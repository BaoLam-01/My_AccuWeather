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
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.adapters.CityNameAdapter
import com.lampro.myaccuweather.adapters.LocationWeatherAdapter
import com.lampro.myaccuweather.base.BaseFragment
import com.lampro.myaccuweather.databinding.FragmentLocationBinding
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.objects.locationdata.LocationItem
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
    private lateinit var key: String
    private lateinit var cityName: String
    private lateinit var countryName: String
    private lateinit var temp: String
    private lateinit var icon: String
    private var lon by Delegates.notNull<Double>()
    private var lat by Delegates.notNull<Double>()
    private lateinit var locationitem: LocationItem
    private lateinit var listLocation: MutableList<LocationItem>
    private lateinit var mcityNameAdapter: CityNameAdapter
    private lateinit var listCities: MutableList<LocationItem>
    private var statusSearch = true


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
        listLocation = mutableListOf()

        if (PrefManager.getListLocation() != null) {

            PrefManager.getListLocation()?.let { listLocation.addAll(it) }
        }

        activity?.let {
            locationClient = LocationServices.getFusedLocationProviderClient(it)
        }

        mlocationWeatherAdapter = LocationWeatherAdapter()
        mlocationWeatherAdapter.updateData(listLocation)
        binding.rvLocationWeather.apply {
            adapter = mlocationWeatherAdapter
            layoutManager = LinearLayoutManager(
                this@LocationFragment.context,
                LinearLayoutManager.VERTICAL,
                false
            )
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
                        key = it.key


                        if (it.localizedName.isNotEmpty()) {
                            cityName = it.localizedName
                        } else {
                            cityName = it.englishName
                        }
                        binding.tvCityName.text = cityName
                        countryName =
                            it.administrativeArea.localizedName + ", " + it.country.localizedName
                        binding.tvCountryName.text = countryName

                        if (statusSearch == true) {
                            binding.llSearchLocation.visibility = View.GONE
                            binding.llCurrentLocation.visibility = View.VISIBLE
                            val params =
                                binding.llRecently.layoutParams as RelativeLayout.LayoutParams
                            params.addRule(RelativeLayout.BELOW, binding.llCurrentLocation.id)
                            binding.llRecently.layoutParams = params
                        } else {
                            binding.llCurrentLocation.visibility = View.GONE
                            binding.llSearchLocation.visibility = View.VISIBLE
                            val params =
                                binding.llRecently.layoutParams as RelativeLayout.LayoutParams
                            params.addRule(RelativeLayout.BELOW, binding.llSearchLocation.id)
                            binding.llRecently.layoutParams = params

                            val locationitem =
                                LocationItem(key, cityName, countryName, temp, icon, lat, lon)
                            listCities.add(locationitem)

                            mcityNameAdapter = CityNameAdapter()
                            mcityNameAdapter.updateData(listCities)
                            binding.rvCitySearch.apply {
                                adapter = mcityNameAdapter
                                layoutManager = LinearLayoutManager(
                                    this.context,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            }
//                            binding.rvCitySearch.setOnClickListener(view.setOnClickListener())
                        }
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
                        if (it.size != 0) {
                            for (i in it) {
                                mlocationViewModel.getLocationKey(i.lat, i.lon)
                                mlocationViewModel.getCurrentWeather(i.lat, i.lon)
                                lat = i.lat
                                lon = i.lon


                            }
                        } else {
                            Toast.makeText(
                                this.context,
                                "No information about ${binding.edtLoactionSearch.text} city was found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this.context,
                        "Get api failed ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        mlocationViewModel.currentWeatherData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {
                        binding.tvTemp.text = it.main.temp.toInt().toString() + "℃"
                        temp = it.main.temp.toInt().toString() + "℃"
                        icon = it.weather[0].icon
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
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
            statusSearch = true
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
                        mlocationViewModel.getCurrentWeather(it.latitude, it.longitude)


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
            PrefManager.setLocationKey(key)
            locationitem = LocationItem(key, cityName, countryName, temp, icon, lat, lon)


            listLocation.removeIf {
                it.cityName.equals(locationitem.cityName)
            }
            listLocation.add(0, locationitem)
            PrefManager.saveListLocation(listLocation)
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
                statusSearch = false
                listCities = mutableListOf()
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
            mlocationViewModel.getCurrentWeather(it.latitude, it.longitude)

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