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
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.adapters.LocationWeatherAdapter
import com.lampro.myaccuweather.base.BaseFragment
import com.lampro.myaccuweather.databinding.FragmentLocationBinding
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.repositories.LocationResponsitory
import com.lampro.myaccuweather.ui.activities.MainActivity
import com.lampro.myaccuweather.utils.PermissionManager
import com.lampro.myaccuweather.utils.PrefManager
import com.lampro.myaccuweather.viewmodels.location.LocationViewModel
import com.lampro.myaccuweather.viewmodels.location.LocationViewModelFactory
import kotlin.properties.Delegates

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LocationFragment : BaseFragment<FragmentLocationBinding>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: MainActivity? = null

    private lateinit var mlocationViewModel: LocationViewModel
    private lateinit var mlocationWeatherAdapter: LocationWeatherAdapter
    private lateinit var locationClient: FusedLocationProviderClient
    private var key = MutableLiveData<String?>()
    private var lon by Delegates.notNull<Double>()
    private var lat by Delegates.notNull<Double>()

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

        activity?.let {
            locationClient = LocationServices.getFusedLocationProviderClient(it)
        }
        initViewModel()

        initView()

        mlocationViewModel.locationKeyData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {

                        if (it.localizedName.isNotEmpty()) {
                            binding.tvCityName.text = it.localizedName
                        } else {
                            binding.tvCityName.text = it.englishName
                        }
                        binding.tvCountryName.text =
                            it.administrativeArea.localizedName + ", " + it.country.localizedName
                        binding.llCurrentLocation.visibility = View.VISIBLE
                    }

                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this.context,
                        "get inf City failed ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
        mlocationViewModel.geoByCityNameData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }
                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {
                        if (response.data.size != 0){
                            mlocationViewModel.getLocationKey(it[0].lat,it[0].lon)
                            lat = it[0].lat
                            lon = it[0].lon
                        }
                        else  {
                            binding.llCurrentLocation.visibility = View.GONE
                            Toast.makeText(this.context, "No information about ${binding.edtLoactionSearch.text} city was found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(this.context, "Get api failed ${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun initViewModel() {
        val application = activity?.application
        val locationResponsitory = LocationResponsitory()
        val locationViewModelFactory =
            LocationViewModelFactory(application!!, locationResponsitory)
        mlocationViewModel =
            ViewModelProvider(
                this,
                factory = locationViewModelFactory
            )[LocationViewModel::class.java]
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
                        mlocationViewModel.getLocationKey(it.latitude, it.longitude)
                        binding.imgLocation.visibility = View.VISIBLE
                        binding.tvCurrentLocation.visibility = View.VISIBLE
                        lat = it.latitude
                        lon = it.longitude
                    }
                }
            }
        }

        binding.llCurrentLocation.setOnClickListener {
            binding.llCurrentLocation.visibility = View.GONE
            binding.imgLocation.visibility = View.GONE
            binding.tvCurrentLocation.visibility = View.GONE
            param2?.apply {
                replaceFragment(HomeWeatherFragment.newInstance(null, param2), "", "")
            }
            PrefManager.setLocation(lat, lon)
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
                mlocationViewModel.getGeoByCityName(binding.edtLoactionSearch.text.toString())
                binding.imgLocation.visibility = View.GONE
                binding.tvCurrentLocation.visibility = View.GONE
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
            mlocationViewModel.getLocationKey(it.latitude, it.longitude)
            binding.imgLocation.visibility = View.VISIBLE
            binding.tvCurrentLocation.visibility = View.VISIBLE

            lat = it.latitude
            lon = it.longitude
        }
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