package com.lampro.myaccuweather.ui.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.adapters.DailyWeatherAdapter
import com.lampro.myaccuweather.base.BaseFragment
import com.lampro.myaccuweather.databinding.FragmentWeatherFor5DaysBinding
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeather
import com.lampro.myaccuweather.repositories.Weather5DayResponsitory
import com.lampro.myaccuweather.ui.activities.MainActivity
import com.lampro.myaccuweather.utils.PrefManager
import com.lampro.myaccuweather.viewmodels.sharedviewmodel.SharedViewModel
import com.lampro.myaccuweather.viewmodels.weather5day.Weather5DayViewModel
import com.lampro.myaccuweather.viewmodels.weather5day.Weather5DayViewModelFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"


class WeatherFor5DaysFragment : BaseFragment<FragmentWeatherFor5DaysBinding>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: MainActivity? = null

    private lateinit var weather5DayViewModel: Weather5DayViewModel
    private lateinit var mDailyWeatherAdapter: DailyWeatherAdapter
    private val viewModel: SharedViewModel by activityViewModels()


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWeatherFor5DaysBinding = FragmentWeatherFor5DaysBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getSerializable(ARG_PARAM2) as MainActivity?
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()





        viewModel.shareData.observe(viewLifecycleOwner) {
            it?.let {
                binding.currentWeather = it
                val epochDateSunSet = it.sys.sunset.toLong()
                var instant = Instant.ofEpochSecond(epochDateSunSet)
                var dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                var formatter = DateTimeFormatter.ofPattern("h:mm a", Locale(PrefManager.getCurrentLang()))
                var formattedDate = dateTime.format(formatter)
                binding.timeSunset.setText(formattedDate)

                val epochDateSunRise = it.sys.sunrise.toLong()
                instant = Instant.ofEpochSecond(epochDateSunRise)
                dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                formattedDate = dateTime.format(formatter)
                binding.timeSunrise.text = formattedDate
            }
        }

        initViewModel()


        weather5DayViewModel.getDailyWeather(
            PrefManager.getLocationLat(),
            PrefManager.getLocationLon()
        )
        weather5DayViewModel.dailyWeatherData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    mDailyWeatherAdapter = DailyWeatherAdapter()
                    response.data?.let {
                        val list = mutableListOf<DailyWeather>()
                        for (i in it.list) {

                            if (currentTime() >= 22) {
                                if (getHour(i.dt) == 22) {
                                    list.add(i)
                                }
                            } else if (getHour(i.dt) == (currentTime() + 0) || getHour(i.dt) == (currentTime() + 1) || getHour(
                                    i.dt
                                ) == (currentTime() + 2)
                            ) {
                                list.add(i)
                            }
                        }
                        Log.d(TAG, "onViewCreated: " + getHour(it.list[0].dt) + currentTime())
                        mDailyWeatherAdapter.updateData(list)
                        binding.rv5Days.apply {
                            adapter = mDailyWeatherAdapter
                            layoutManager = LinearLayoutManager(
                                this@WeatherFor5DaysFragment.context,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                }
            }

        }

        weather5DayViewModel.getAirQuality(
            PrefManager.getLocationLat(),
            PrefManager.getLocationLon()
        )
        weather5DayViewModel.airQualityData.observe(viewLifecycleOwner)
        { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {
                        var airQuality = it.list[0].main.aqi.toString() + " - "
                        when (it.list[0].main.aqi) {
                            in 1..3 -> airQuality += getString(R.string.air_good)
                            in 4..6 -> airQuality += getString(R.string.air_medium)
                            in 7..10 -> airQuality += getString(R.string.air_polluted)
                            else -> airQuality += getString(R.string.air_danger)
                        }
                        binding.tvAirQuality.text = airQuality
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this.context,
                        "Get air quality failed ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
        weather5DayViewModel.getLocationKey(
            PrefManager.getLocationLat(),
            PrefManager.getLocationLon()
        )
        weather5DayViewModel.locationKeyData.observe(viewLifecycleOwner)
        { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {
                        getUvIndex(it.key)
                        if (it.localizedName.isNotEmpty()) {
                            binding.tvCityName.text = it.localizedName
                        } else {
                            binding.tvCityName.text = it.englishName
                        }
                        binding.tvCountryName.text =
                            it.administrativeArea.localizedName + ", " + it.country.localizedName

                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this.context,
                        "Get UV Index failed ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun getUvIndex(locationKey: String) {
        weather5DayViewModel.getUvIndex(locationKey)
        weather5DayViewModel.uvIndexData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {
                        binding.tvUvIndex.text = it[0].uVIndex.toString()
                        binding.tvUvIndex2.text = it[0].uVIndexText
                        binding.btnSeeMore.isClickable = true
                        var url: String
                        url = it[0].mobileLink
                        binding.btnSeeMore.setOnClickListener(View.OnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        })
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this.context,
                        "Get UV Index failed ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.btnSeeMore.isClickable = false
                }
            }
        }
    }


    private fun initViewModel() {
        val application = activity?.application
        val weather5DayResponsitory = Weather5DayResponsitory()
        val weather5DayViewModelFactory =
            Weather5DayViewModelFactory(application!!, weather5DayResponsitory)
        weather5DayViewModel =
            ViewModelProvider(
                this,
                factory = weather5DayViewModelFactory
            )[Weather5DayViewModel::class.java]
    }

    private fun initView() {
        binding.btnBack.setOnClickListener {

            param2?.apply {
                supportFragmentManager.beginTransaction().remove(this@WeatherFor5DaysFragment)
                    .commit()
            }
        }
    }

    fun getHour(dt: Int): Int {
        val epochSeconds = dt.toLong()
        val instant = Instant.ofEpochSecond(epochSeconds)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("HH")
        val formattedDate = dateTime.format(formatter)
        return formattedDate.toInt()
    }

    fun currentTime(): Int {
        val currentTime = Calendar.getInstance().time
        val formattedTime = SimpleDateFormat("HH", Locale.getDefault()).format(currentTime)
        return formattedTime.toInt()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String?, param2: MainActivity?) =
            WeatherFor5DaysFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                }
            }
    }


}