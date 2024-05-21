package com.lampro.myaccuweather.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lampro.myaccuweather.adapters.LocationWeatherAdapter
import com.lampro.myaccuweather.databinding.FragmentLocationBinding
import com.lampro.myaccuweather.ui.activities.MainActivity
import com.lampro.weatherapp.base.BaseFragment
import com.lampro.weatherapp.repositories.WeatherRepository
import com.lampro.weatherapp.utils.PermissionManager
import com.lampro.weatherapp.viewmodels.WeatherViewModel
import com.lampro.weatherapp.viewmodels.WeatherViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LocationFragment : BaseFragment<FragmentLocationBinding>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: MainActivity? = null

    private lateinit var mWeatherViewModel: WeatherViewModel
    private lateinit var mlocationWeatherAdapter: LocationWeatherAdapter
    private lateinit var locationClient: FusedLocationProviderClient
    private var key = MutableLiveData<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getSerializable(ARG_PARAM2) as MainActivity?
        }
    }


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLocationBinding = FragmentLocationBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        activity?.let {
            locationClient = LocationServices.getFusedLocationProviderClient(it)
        }

        initView()

    }
    private fun initViewModel() {
        val application = activity?.application
        val weatherRepository = WeatherRepository()
        val weatherViewModelFactory = WeatherViewModelFactory(application!!, weatherRepository)
        mWeatherViewModel =
            ViewModelProvider(this, factory = weatherViewModelFactory)[WeatherViewModel::class.java]
    }
    private fun initView() {
        binding.imgBtnLocationSearch.setOnClickListener {
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
                    Log.d(ContentValues.TAG, "initView: click")
                } else {
                    locationClient.lastLocation.addOnSuccessListener {
                        Log.d(ContentValues.TAG, "initView: lat: ${it.latitude} | long: ${it.longitude}")

//                        key = getLocationKey(it.latitude, it.longitude)
//                        key.observe(viewLifecycleOwner) { key ->
//                            if (key != null) {
//
//                            } else {
//                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
//                            }
//                        }

                    }
                }
            }
        }
        binding.btnBack.setOnClickListener {
            param2?.apply {
                supportFragmentManager.beginTransaction().remove(this@LocationFragment).commit()
            }
        }

        binding.imgBtnSearch.setOnClickListener {
            if (binding.edtLoactionSearch.text.isEmpty()) {
                Toast.makeText(context, "Enter yout location!", Toast.LENGTH_SHORT).show()
            }else{

            }
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
//            key = getLocationKey(it.latitude, it.longitude)
//            key.observe(viewLifecycleOwner) { key ->
//                if (key != null) {
//
//                } else {
//                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
//                }
//            }

        }
    }
    private fun getLocationKey(latitude: Double, longitude: Double): MutableLiveData<String?> {
        mWeatherViewModel.getLocationKey(latitude, longitude)
        mWeatherViewModel.locationKeyData.observe(viewLifecycleOwner) { response ->
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
        fun newInstance(param1: String, param2: MainActivity) =
            LocationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                }
            }
    }
}