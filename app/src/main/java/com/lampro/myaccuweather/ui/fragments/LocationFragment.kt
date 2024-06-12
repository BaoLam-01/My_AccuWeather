package com.lampro.myaccuweather.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.media.MediaRouter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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


class LocationFragment : BaseFragment<FragmentLocationBinding>(), CityNameAdapter.IOnItemClick,
    LocationWeatherAdapter.IOnLocationClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: MainActivity? = null

    private lateinit var mlocationViewModel: LocationViewModel
    private lateinit var mlocationWeatherAdapter: LocationWeatherAdapter
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var key: String
    private lateinit var cityName: String
    private lateinit var countryName: String
    private var temp by Delegates.notNull<Int>()
    private lateinit var icon: String
    private var lon by Delegates.notNull<Double>()
    private var lat by Delegates.notNull<Double>()
    private lateinit var locationItem: LocationItem
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
        mlocationWeatherAdapter.setCallBack(this)
        mlocationWeatherAdapter.updateData(listLocation)
        binding.rvLocationWeather.apply {
            adapter = mlocationWeatherAdapter
            layoutManager = LinearLayoutManager(
                this@LocationFragment.context,
                LinearLayoutManager.VERTICAL,
                false
            )
        }

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var position = viewHolder.adapterPosition
                listLocation.removeAt(position)
                mlocationWeatherAdapter.notifyItemRemoved(position)
                PrefManager.saveListLocation(listLocation)
            }

        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvLocationWeather)

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
                            mcityNameAdapter.setCallBack(this)
                            mcityNameAdapter.updateData(listCities)
                            binding.rvCitySearch.apply {
                                adapter = mcityNameAdapter
                                layoutManager = LinearLayoutManager(
                                    this.context,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            }
                        }
                    }

                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this.context,
                        getString(R.string.call_api_failed, response.message),
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
                                getString(R.string.city_not_found, binding.edtLoactionSearch.text),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this.context,
                        getString(R.string.call_api_failed, response.message),
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
                        binding.tvTemp.text = it.main.temp.toInt().toString() + PrefManager.getCurrentUnits()
                        if (PrefManager.getCurrentUnits() == "℃") {
                            temp = Math.ceil(it.main.temp).toInt()
                        } else {
                            val f = it.main.temp
                            val c = 5/9f * (f - 32)
                            temp = c.toInt()
                        }
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
            binding.edtLoactionSearch.text = null
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

            param2?.apply {
                PrefManager.setLocation(lat, lon)
                PrefManager.setLocationKey(key)
                locationItem = LocationItem(key, cityName, countryName, temp, icon, lat, lon)


                listLocation.removeIf {
                    it.cityName.equals(locationItem.cityName)
                }
                listLocation.add(0, locationItem)
                PrefManager.saveListLocation(listLocation)

                replaceFragment(HomeWeatherFragment.newInstance(null, param2), "", "")
            }

        }


        binding.btnBack.setOnClickListener {
            param2?.apply {
                supportFragmentManager.beginTransaction().remove(this@LocationFragment).commit()
            }
        }


        binding.imgBtnSearch.setOnClickListener {
            if (binding.edtLoactionSearch.text.isEmpty()) {
                Toast.makeText(context, getString(R.string.enter_yout_location), Toast.LENGTH_SHORT).show()
            } else {
                mlocationViewModel.getGeoByCityName(binding.edtLoactionSearch.text.toString())
                statusSearch = false
                listCities = mutableListOf()
            }
        }
        binding.edtLoactionSearch.setOnEditorActionListener{_ ,actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (binding.edtLoactionSearch.text.isEmpty()) {
                    Toast.makeText(context, getString(R.string.enter_yout_location), Toast.LENGTH_SHORT).show()
                } else {
                    mlocationViewModel.getGeoByCityName(binding.edtLoactionSearch.text.toString())
                    statusSearch = false
                    listCities = mutableListOf()
                }
                return@setOnEditorActionListener true
            }
            false
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
            mlocationViewModel.getLocationKey(it.latitude, it.longitude)
            mlocationViewModel.getCurrentWeather(it.latitude, it.longitude)

            lat = it.latitude
            lon = it.longitude

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
        val btnView2 = dialog?.findViewById<Button>(R.id.btnSetting)
        if (btnView2 != null) {
            btnView2.setOnClickListener {
                if (dialog != null) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package","com.lampro.myaccuweather", null)
                    intent.data = uri

                    startActivity(intent)

                    dialog.dismiss()
                }
            }
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

    override fun onItemClick() {
        param2?.apply {
            PrefManager.setLocation(lat, lon)
            PrefManager.setLocationKey(key)
            locationItem = LocationItem(key, cityName, countryName, temp, icon, lat, lon)


            listLocation.removeIf {
                it.cityName.equals(locationItem.cityName)
            }
            listLocation.add(0, locationItem)
            PrefManager.saveListLocation(listLocation)

            replaceFragment(HomeWeatherFragment.newInstance(null, param2), "", "")
        }
    }

    override fun onLocationClick(item: LocationItem) {
        param2?.apply {
            PrefManager.setLocation(item.lat, item.lon)
            PrefManager.setLocationKey(item.locationKey)
            locationItem = item


            listLocation.removeIf {
                it.cityName.equals(locationItem.cityName)
            }
            listLocation.add(0, locationItem)
            PrefManager.saveListLocation(listLocation)

            replaceFragment(HomeWeatherFragment.newInstance(null, param2), "", "")
        }
    }

}