package com.lampro.myaccuweather.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.base.BaseFragment
import com.lampro.myaccuweather.databinding.FragmentLocationBinding
import com.lampro.myaccuweather.repositories.HomeWeatherRepository
import com.lampro.myaccuweather.utils.PermissionManager
import com.lampro.myaccuweather.adapters.LocationWeatherAdapter
import com.lampro.myaccuweather.ui.activities.MainActivity
import com.lampro.myaccuweather.viewmodels.HomeWeather.HomeWeatherViewModel
import com.lampro.myaccuweather.viewmodels.HomeWeather.HomeWeatherViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LocationFragment : BaseFragment<FragmentLocationBinding>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: MainActivity? = null

    private lateinit var mHomeWeatherViewModel: HomeWeatherViewModel
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


        initView()


//        initViewModel()


        activity?.let {
            locationClient = LocationServices.getFusedLocationProviderClient(it)
        }


    }

    private fun initViewModel() {
        val application = activity?.application
        val weatherRepository = HomeWeatherRepository()
        val homeWeatherViewModelFactory =
            HomeWeatherViewModelFactory(application!!, weatherRepository)
        mHomeWeatherViewModel =
            ViewModelProvider(
                this,
                factory = homeWeatherViewModelFactory
            )[HomeWeatherViewModel::class.java]
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
                        Log.d(
                            ContentValues.TAG,
                            "initView: lat: ${it.latitude} | long: ${it.longitude}"
                        )

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
            } else {
//                var listLocation: MutableList<Location> = mutableListOf()
//                mWeatherViewModel.
//                listLocation.add(Location("${binding.edtLoactionSearch.text}",""))
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
        return key
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: MainActivity?) =
            LocationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                }
            }
    }

}