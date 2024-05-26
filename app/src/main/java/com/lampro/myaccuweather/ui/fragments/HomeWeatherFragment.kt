package com.lampro.myaccuweather.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.adapters.HourlyWeatherAdapter
import com.lampro.myaccuweather.base.BaseFragment
import com.lampro.myaccuweather.databinding.FragmentHomeWeatherBinding
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponse
import com.lampro.myaccuweather.repositories.HomeWeatherRepository
import com.lampro.myaccuweather.utils.PermissionManager
import com.lampro.myaccuweather.ui.activities.MainActivity
import com.lampro.myaccuweather.utils.PrefManager
import com.lampro.myaccuweather.viewmodels.homeweather.HomeWeatherViewModel
import com.lampro.myaccuweather.viewmodels.homeweather.HomeWeatherViewModelFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var homeWeatherViewModel: HomeWeatherViewModel

private lateinit var mHourlyWeatherAdapter: HourlyWeatherAdapter
private lateinit var locationClient: FusedLocationProviderClient

class HomeWeatherFragment : BaseFragment<FragmentHomeWeatherBinding>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: MainActivity? = null
    private var currentWeatherResponse: CurrentWeatherResponse? = null
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var locationKey = MutableLiveData<String>()
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


        if (PrefManager.getLocationLat() == 0.0 || PrefManager.getLocationLon() == 0.0) {
            lat = 35.6895
            lon = 139.6917

        } else {
            lat = PrefManager.getLocationLat()
            lon = PrefManager.getLocationLon()
        }

        activity?.let { locationClient = LocationServices.getFusedLocationProviderClient(it) }


        initView()


        getCurrentWeather()
        locationKey = getLocationKey()
        locationKey.observe(viewLifecycleOwner) { key ->
            if (key != null) {
                getHourlyWeather(key)
            } else {
                Toast.makeText(this.context, "Get location key failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getHourlyWeather(locationKey: String) {
        homeWeatherViewModel.getHourlyWeather(locationKey)
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
                                this@HomeWeatherFragment.context,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        }
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this@HomeWeatherFragment.context,
                        "get hourly weather failed ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getCurrentWeather() {
        homeWeatherViewModel.getCurrentWeather(lat, lon)
        homeWeatherViewModel.currentWeatherData.observe(viewLifecycleOwner) { response ->

            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {
                        //
                        binding.currentWeatherResponse = it
                        currentWeatherResponse = it
                        val epochSeconds = it.dt.toLong()
                        val instant = Instant.ofEpochSecond(epochSeconds)
                        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                        val formatter = DateTimeFormatter.ofPattern("MMM, dd", Locale.ENGLISH)
                        val formattedDate = dateTime.format(formatter)
                        binding.tvCurrentDay.setText(formattedDate)
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this@HomeWeatherFragment.context,
                        "get curent weather failed ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(TAG, "getCurrentWeather: ${response.message}")
                }
            }
        }
    }


    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentHomeWeatherBinding = FragmentHomeWeatherBinding.inflate(layoutInflater)

    private fun initViewModel() {
        val application = activity?.application
        val weatherRepository = HomeWeatherRepository()
        val homeWeatherViewModelFactory =
            HomeWeatherViewModelFactory(application!!, weatherRepository)
        homeWeatherViewModel =
            ViewModelProvider(
                this,
                factory = homeWeatherViewModelFactory
            )[HomeWeatherViewModel::class.java]
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
                        lat = it.latitude
                        lon = it.longitude
                        PrefManager.setLocation(it.latitude, it.longitude)


                        homeWeatherViewModel.getCurrentWeather(it.latitude, it.longitude)


                        locationKey = getLocationKey()
                        locationKey.observe(viewLifecycleOwner) { key ->
                            if (key != null) {

                                homeWeatherViewModel.getHourlyWeather(key)
                            } else {
                                Toast.makeText(
                                    this.context,
                                    "Get location key failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }


            }
        }

        binding.btnMenu.setOnClickListener {
            param2?.addFragment(
                WeatherFor5DaysFragment.newInstance(
                    currentWeatherResponse,
                    param2,
                ), "", ""
            )
        }
        binding.btnPlusCircle.setOnClickListener {
            param2?.addFragment(LocationFragment.newInstance("", param2), "", "")
        }

    }

    fun getLocationKey(): MutableLiveData<String> {
        var key = MutableLiveData<String>()
        homeWeatherViewModel.getLocationKey(lat, lon)
        homeWeatherViewModel.locationKeyData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {
                        key.value = it.key
                        getHourlyWeather(it.key)
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this@HomeWeatherFragment.context,
                        "get location key failed ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(TAG, "getLocationKey: ${response.message}")
                }
            }
        }
        return key
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
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) &&
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)
                val alertDialog = AlertDialog.Builder(this.context)
                    .setView(dialogView)
                    .create()
                val btnView = dialogView.findViewById<Button>(R.id.OK)

                alertDialog.show()
                btnView.setOnClickListener { alertDialog.dismiss() }

            } else {
                return@registerForActivityResult
            }
        } else locationClient.lastLocation.addOnSuccessListener {
            Log.d("TAG", ": lat: ${it.latitude} | long: ${it.longitude}")


            lat = it.latitude
            lon = it.longitude
            PrefManager.setLocation(it.latitude, it.longitude)


            homeWeatherViewModel.getCurrentWeather(it.latitude, it.longitude)



            locationKey = getLocationKey()
            locationKey.observe(viewLifecycleOwner) { key ->
                if (key != null) {

                    homeWeatherViewModel.getHourlyWeather(key)
                } else {
                    Toast.makeText(this.context, "Get location key failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String?, param2: MainActivity?) = HomeWeatherFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putSerializable(ARG_PARAM2, param2)
            }
        }
    }


}