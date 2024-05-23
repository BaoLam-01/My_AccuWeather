package com.lampro.myaccuweather.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lampro.myaccuweather.base.BaseFragment
import com.lampro.myaccuweather.databinding.FragmentWeatherFor7DaysBinding
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.repositories.WeatherRepository
import com.lampro.myaccuweather.adapters.DailyWeatherAdapter
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponseItem
import com.lampro.myaccuweather.ui.activities.MainActivity
import com.lampro.myaccuweather.viewmodels.HomeWeather.HomeWeatherViewModel
import com.lampro.myaccuweather.viewmodels.HomeWeather.HomeWeatherViewModelFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"


class WeatherFor7DaysFragment : BaseFragment<FragmentWeatherFor7DaysBinding>(){
    // TODO: Rename and change types of parameters
    private var param1: CurrentWeatherResponseItem? = null
    private var param2: MainActivity? = null
    private var param3: String? = null

    private lateinit var homeWeatherViewModel: HomeWeatherViewModel
    private lateinit var mDailyWeatherAdapter: DailyWeatherAdapter
    private var key = "226396"


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWeatherFor7DaysBinding = FragmentWeatherFor7DaysBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as CurrentWeatherResponseItem?
            param2 = it.getSerializable(ARG_PARAM2) as MainActivity?
            param3 = it.getString(ARG_PARAM3)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        initViewModel()
        if (param3 != null) {
            key = param3.toString()
        }

        homeWeatherViewModel.getCityName(key)
        homeWeatherViewModel.cityNameData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                binding.tvCityName.text = response
            } else {
                binding.tvCityName.text = "No data"
            }
        }
        homeWeatherViewModel.getDailyWeather(key)
        homeWeatherViewModel.dailyWeatherData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    mDailyWeatherAdapter = DailyWeatherAdapter()
                    response.data?.let {
                        mDailyWeatherAdapter.updateData(it.dailyForecasts)
                        binding.rv7Days.apply {
                            adapter = mDailyWeatherAdapter
                            layoutManager = LinearLayoutManager(
                                this@WeatherFor7DaysFragment.context,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this@WeatherFor7DaysFragment.context,
                        "get daily weather ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        homeWeatherViewModel.getDaily1DayWeather(key)
        homeWeatherViewModel._1DayWeatherData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {
                        binding.dailyForecasts = it.dailyForecasts[0]
                        binding.tvAirQuality.text =
                            "${it.dailyForecasts.get(0).airAndPollen.get(0).categoryValue}-" +
                                    it.dailyForecasts.get(0).airAndPollen.get(0).category
                        val epochDateSunSet = it.dailyForecasts[0].sun.epochSet.toLong()
                        var instant = Instant.ofEpochSecond(epochDateSunSet)
                        var dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                        var formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)
                        var formattedDate = dateTime.format(formatter)
                        binding.timeSunset.setText(formattedDate)

                        val epochDateSunRise = it.dailyForecasts[0].sun.epochRise.toLong()
                        instant = Instant.ofEpochSecond(epochDateSunRise)
                        dateTime = LocalDateTime.ofInstant(instant,ZoneId.systemDefault())
                        formattedDate = dateTime.format(formatter)
                        binding.timeSunrise.text = formattedDate
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this@WeatherFor7DaysFragment.context,
                        "get daily 1 day weather ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(TAG, "onViewCreated: ${response.message}")
                }
            }
        }




        param1?.let {
            binding.currenWeather = it
        }

    }


    private fun initViewModel() {
        val application = activity?.application
        val weatherRepository = WeatherRepository()
        val homeWeatherViewModelFactory = HomeWeatherViewModelFactory(application!!, weatherRepository)
        homeWeatherViewModel =
            ViewModelProvider(this, factory = homeWeatherViewModelFactory)[HomeWeatherViewModel::class.java]
    }
    private fun initView(){
        binding.btnBack.setOnClickListener {

            param2?.apply {
                supportFragmentManager.beginTransaction().remove(this@WeatherFor7DaysFragment).commit()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: CurrentWeatherResponseItem?, param2: MainActivity?, param3: String?) =
            WeatherFor7DaysFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                    putString(ARG_PARAM3,param3)
                }
            }
    }

}