package com.lampro.myaccuweather.ui.fragments

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
import com.lampro.myaccuweather.adapters.HourlyWeatherAdapter
import com.lampro.myaccuweather.base.BaseFragment
import com.lampro.myaccuweather.databinding.FragmentHomeWeatherBinding
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.repositories.WeatherRepository
import com.lampro.myaccuweather.utils.PermissionManager
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponseItem
import com.lampro.myaccuweather.ui.activities.MainActivity
import com.lampro.myaccuweather.viewmodels.HomeWeather.HomeWeatherViewModel
import com.lampro.myaccuweather.viewmodels.HomeWeather.HomeWeatherViewModelFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

private lateinit var homeWeatherViewModel: HomeWeatherViewModel

private lateinit var mHourlyWeatherAdapter: HourlyWeatherAdapter
private lateinit var locationClient: FusedLocationProviderClient

class HomefWeatherFragment : BaseFragment<FragmentHomeWeatherBinding>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: MainActivity? = null
    private var param3: String? = null
    private  var currentWeatherResponse: CurrentWeatherResponseItem? = null
    private var key = MutableLiveData<String?>()
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


        homeWeatherViewModel.getCurrentWeather("226396")
        homeWeatherViewModel.currentWeatherData.observe(viewLifecycleOwner) { response ->

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
                        this@HomefWeatherFragment.context,
                        "get curent weather ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        homeWeatherViewModel.getHourlyWeather("226396")
        homeWeatherViewModel.hourlyWeatherData.observe(viewLifecycleOwner) { response ->
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
                                this@HomefWeatherFragment.context,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this@HomefWeatherFragment.context,
                        "get hourly weather ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentHomeWeatherBinding = FragmentHomeWeatherBinding.inflate(layoutInflater)

    private fun initViewModel() {
        val application = activity?.application
        val weatherRepository = WeatherRepository()
        val homeWeatherViewModelFactory = HomeWeatherViewModelFactory(application!!, weatherRepository)
        homeWeatherViewModel =
            ViewModelProvider(this, factory = homeWeatherViewModelFactory)[HomeWeatherViewModel::class.java]
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

                        key = getLocationKey(it.latitude, it.longitude)
                        key.observe(viewLifecycleOwner) { key ->
                            if (key != null) {

                                homeWeatherViewModel.getCurrentWeather(key)
                                homeWeatherViewModel.getHourlyWeather(key)
                            } else {
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }
            }
        }

        binding.btnMenu.setOnClickListener {
            param2?.addFragment(WeatherFor7DaysFragment.newInstance(currentWeatherResponse, param2,param3), "", "")
        }
        binding.btnPlusCircle.setOnClickListener{
            param2?.addFragment(LocationFragment.newInstance("", param2), "", "")
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
            key = getLocationKey(it.latitude, it.longitude)
            key.observe(viewLifecycleOwner) { key ->
                if (key != null) {

                    homeWeatherViewModel.getCurrentWeather(key)
                    homeWeatherViewModel.getHourlyWeather(key)
                } else {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun getLocationKey(latitude: Double, longitude: Double): MutableLiveData<String?> {
        homeWeatherViewModel.getLocationKey(latitude, longitude)
        homeWeatherViewModel.locationKeyData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                key.value = response
                param3 = response
            } else {
                key.value = ""
            }
        }
        return key
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String?, param2: MainActivity?) = HomefWeatherFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putSerializable(ARG_PARAM2, param2)
            }
        }
    }


}