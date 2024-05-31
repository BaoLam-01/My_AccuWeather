package com.lampro.myaccuweather.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
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
import com.lampro.myaccuweather.ui.activities.MainActivity
import com.lampro.myaccuweather.utils.PermissionManager
import com.lampro.myaccuweather.utils.PrefManager
import com.lampro.myaccuweather.viewmodels.homeweather.HomeWeatherViewModel
import com.lampro.myaccuweather.viewmodels.homeweather.HomeWeatherViewModelFactory
import com.lampro.myaccuweather.viewmodels.mainviewmodel.MainViewModel
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


        if (PrefManager.getLocationLat() == 0.0 || PrefManager.getLocationLon() == 0.0 || PrefManager.getLocationKey() == null) {
            lat = 35.6895
            lon = 139.6917
            homeWeatherViewModel.getCurrentWeather(lat, lon)
            homeWeatherViewModel.getLocationKey(lat, lon)
        } else {
            lat = PrefManager.getLocationLat()
            lon = PrefManager.getLocationLon()
            homeWeatherViewModel.getLocationKey(lat, lon)
            homeWeatherViewModel.getCurrentWeather(lat, lon)
        }

        activity?.let { locationClient = LocationServices.getFusedLocationProviderClient(it) }


        initView()



        homeWeatherViewModel.locationKeyData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {
                        homeWeatherViewModel.getHourlyWeather(it.key)
                        PrefManager.setLocationKey(it.key)
                        PrefManager.setLocation(lat, lon)
                        if (it.localizedName.isNotEmpty()){
                            binding.tvCityName.text = it.localizedName
                        }else{
                            binding.tvCityName.text= it.englishName
                        }
                        binding.tvCountryName.text = it.administrativeArea.localizedName + ", " + it.country.localizedName

                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this.context,
                        "Get location key Failed ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
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
                        homeWeatherViewModel.getLocationKey(it.latitude, it.longitude)

                        homeWeatherViewModel.getCurrentWeather(it.latitude, it.longitude)

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

                showAlertDialog()

            } else {
                return@registerForActivityResult
            }
        } else locationClient.lastLocation.addOnSuccessListener {
            Log.d("TAG", ": lat: ${it.latitude} | long: ${it.longitude}")



            lat = it.latitude
            lon = it.longitude
            homeWeatherViewModel.getLocationKey(it.latitude, it.longitude)

            homeWeatherViewModel.getCurrentWeather(it.latitude, it.longitude)
        }
    }

    private fun showAlertDialog() {
        val dialog = this.context?.let { Dialog(it) }
        if (dialog != null) {
            dialog.setContentView(R.layout.dialog_custom)
            dialog.window?.setBackgroundDrawable(ColorDrawable(0))
            dialog.show()
        }
        val btnView1 = dialog?.findViewById<Button>(R.id.OK)
        if (btnView1 != null) {
            btnView1.setOnClickListener {
                if (dialog != null) {
                    dialog.dismiss()
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