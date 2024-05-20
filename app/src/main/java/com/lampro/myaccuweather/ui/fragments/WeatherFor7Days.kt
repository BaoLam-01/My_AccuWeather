package com.lampro.weatherapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lampro.myaccuweather.adapters.DailyWeatherAdapter
import com.lampro.myaccuweather.databinding.FragmentWeatherFor7DaysBinding
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponseItem
import com.lampro.myaccuweather.ui.activities.MainActivity
import com.lampro.weatherapp.adapters.HourlyWeatherAdapter
import com.lampro.weatherapp.base.BaseFragment
import com.lampro.weatherapp.network.api.ApiResponse
import com.lampro.weatherapp.repositories.WeatherRepository
import com.lampro.weatherapp.viewmodels.WeatherViewModel
import com.lampro.weatherapp.viewmodels.WeatherViewModelFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class WeatherFor7Days : BaseFragment<FragmentWeatherFor7DaysBinding>() {
    // TODO: Rename and change types of parameters
    private var param1: CurrentWeatherResponseItem? = null
    private var param2: MainActivity? = null

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var mDailyWeatherAdapter: DailyWeatherAdapter


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWeatherFor7DaysBinding = FragmentWeatherFor7DaysBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as CurrentWeatherResponseItem?
            param2 = it.getSerializable(ARG_PARAM2) as MainActivity?
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        weatherViewModel.getCityName("226396")
        weatherViewModel.cityName.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                binding.tvCityName.text = response

            } else {
                binding.tvCityName.text = "No data"
            }
        }
        weatherViewModel.getDailyWeather("226396")
        weatherViewModel.dailyWeatherData.observe(viewLifecycleOwner) { response ->
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
                                this@WeatherFor7Days.context,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this@WeatherFor7Days.context,
                        "get hourly weather ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        weatherViewModel.getDaily1DayWeather("226396")
        weatherViewModel._1DayWeatherData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {
                        binding.dailyForecasts = it.dailyForecasts[0]
                        binding.tvAirQuality.text =
                            it.dailyForecasts[0].airAndPollen[0].categoryValue.toString() + " - " +
                                    it.dailyForecasts[0].airAndPollen[0].category.toString()
                        val epochDateSunSet = it.dailyForecasts[0].sun.epochSet.toLong()
                        var instant = Instant.ofEpochSecond(epochDateSunSet)
                        var dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                        var formatter = DateTimeFormatter.ofPattern("h:mm a")
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
                        this@WeatherFor7Days.context,
                        "get hourly weather ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }



        initView()

        param1?.let {
            binding.currenWeather = it
        }

    }

    private fun initView() {
        binding.btnMenu.setOnClickListener {
            param2?.apply {
                supportFragmentManager.beginTransaction().remove(this@WeatherFor7Days).commit()
            }
        }
    }

    private fun initViewModel() {
        val application = activity?.application
        val weatherRepository = WeatherRepository()
        val weatherViewModelFactory = WeatherViewModelFactory(application!!, weatherRepository)
        weatherViewModel =
            ViewModelProvider(this, factory = weatherViewModelFactory)[WeatherViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: CurrentWeatherResponseItem?, param2: MainActivity?) =
            WeatherFor7Days().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                }
            }
    }
}