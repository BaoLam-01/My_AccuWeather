package com.lampro.weatherapp.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lampro.myaccuweather.databinding.FragmentInfWeatherBinding
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponseItem
import com.lampro.myaccuweather.ui.activities.MainActivity
import com.lampro.weatherapp.adapters.HourlyWeatherAdapter
import com.lampro.weatherapp.base.BaseFragment
import com.lampro.weatherapp.network.api.ApiResponse
import com.lampro.weatherapp.repositories.WeatherRepository
import com.lampro.weatherapp.utils.PermissionManager
import com.lampro.weatherapp.viewmodels.WeatherViewModel
import com.lampro.weatherapp.viewmodels.WeatherViewModelFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var weatherViewModel: WeatherViewModel

private lateinit var mHourlyWeatherAdapter: HourlyWeatherAdapter
private lateinit var locationClient: FusedLocationProviderClient

class InfWeather : BaseFragment<FragmentInfWeatherBinding>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: MainActivity? = null
    private lateinit var currentWeatherResponse : CurrentWeatherResponseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getSerializable(ARG_PARAM2) as MainActivity?
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        activity?.let { locationClient = LocationServices.getFusedLocationProviderClient(it) }

        initView()


        weatherViewModel.getCurrentWeather("226396")
        weatherViewModel.currentWeatherData.observe(viewLifecycleOwner) { response ->

            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {
//
                        binding.currentWeatherResponse = it[0]
                        currentWeatherResponse = it[0]
                        val epochSeconds = it[0].epochTime.toLong()
                        val instant = Instant.ofEpochSecond(epochSeconds)
                        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                        val formatter = DateTimeFormatter.ofPattern("MMM,dd", Locale.ENGLISH)
                        val formattedDate = dateTime.format(formatter)
                        binding.tvCurrentDay.setText(formattedDate)
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this@InfWeather.context,
                        "get curent weather ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        weatherViewModel.getHourlyWeather("226396")
        weatherViewModel.hourlyWeatherData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    mHourlyWeatherAdapter = HourlyWeatherAdapter()
                    response.data?.let {
                        mHourlyWeatherAdapter.updateData(it)
                        binding.rvWeatherHour.apply {
                            adapter = mHourlyWeatherAdapter
                            layoutManager = LinearLayoutManager(
                                this@InfWeather.context,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this@InfWeather.context,
                        "get hourly weather ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentInfWeatherBinding = FragmentInfWeatherBinding.inflate(layoutInflater)

    private fun initViewModel() {
        val application = activity?.application
        val weatherRepository = WeatherRepository()
        val weatherViewModelFactory = WeatherViewModelFactory(application!!, weatherRepository)
        weatherViewModel =
            ViewModelProvider(this, factory = weatherViewModelFactory)[WeatherViewModel::class.java]
    }

    private fun initView() {

        binding.btnLocation.setOnClickListener {
            activity?.let { it1 ->
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    PermissionManager.requestLocationPermission(
                        it1, locationResultLauncher
                    )
                    Log.d(TAG, "initView: click")
                } else {
                    locationClient.lastLocation.addOnSuccessListener {
                        Log.d(TAG, "initView: lat: ${it.latitude} | long: ${it.longitude}")
//                        Toast.makeText(context,getLocationKey(it.latitude, it.longitude), Toast.LENGTH_SHORT).show()

                        val hh = getLocationKey(it.latitude, it.longitude)
                        hh.observe(viewLifecycleOwner){
                                hh ->
                            if (hh != null){

                                weatherViewModel.getCurrentWeather(hh)
                                weatherViewModel.getHourlyWeather(hh)
                            }else{
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }
            }
        }

        binding.btnMenu.setOnClickListener {
            param2?.addFragment(WeatherFor7Days.newInstance(currentWeatherResponse, param2), "", "")
        }

    }



    @SuppressLint("MissingPermission")
    private val locationResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@registerForActivityResult
        } else locationClient.lastLocation.addOnSuccessListener {
            Log.d("TAG", ": lat: ${it.latitude} | long: ${it.longitude}")
            val hh = getLocationKey(it.latitude, it.longitude)
            hh.observe(viewLifecycleOwner){
                hh ->
                if (hh != null){

                    weatherViewModel.getCurrentWeather(hh)
                    weatherViewModel.getHourlyWeather(hh)
                }else{
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun getLocationKey(latitude: Double, longitude: Double): MutableLiveData<String?> {
        var key = MutableLiveData<String?>()
        weatherViewModel.getLocationKey(latitude, longitude)
        weatherViewModel.locationKeyData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                key.value = response
            } else {
                key.value = ""
            }
        }
        return key
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String?, param2: MainActivity?) = InfWeather().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putSerializable(ARG_PARAM2, param2)
            }
        }
    }
}