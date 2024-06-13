package com.lampro.myaccuweather.ui.fragments

import android.Manifest
import android.R.drawable
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ColorSpace
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.adapters.AdapterViewBindingAdapter.OnItemSelected
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.adapters.HourlyRainAdapter
import com.lampro.myaccuweather.adapters.HourlyWeatherAdapter
import com.lampro.myaccuweather.adapters.LanguageAdapter
import com.lampro.myaccuweather.adapters.UnitsAdapter
import com.lampro.myaccuweather.base.BaseFragment
import com.lampro.myaccuweather.databinding.FragmentHomeWeatherBinding
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.repositories.HomeWeatherRepository
import com.lampro.myaccuweather.ui.activities.MainActivity
import com.lampro.myaccuweather.utils.PermissionManager
import com.lampro.myaccuweather.utils.PrefManager
import com.lampro.myaccuweather.viewmodels.homeweather.HomeWeatherViewModel
import com.lampro.myaccuweather.viewmodels.homeweather.HomeWeatherViewModelFactory
import com.lampro.myaccuweather.viewmodels.sharedviewmodel.SharedViewModel
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
private lateinit var mHourlyRainAdapter: HourlyRainAdapter
private lateinit var locationClient: FusedLocationProviderClient

class HomeWeatherFragment : BaseFragment<FragmentHomeWeatherBinding>() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: MainActivity? = null
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var unitsAdapter: UnitsAdapter
    private var isSpinerInitialized = false
    private var isSpinerUnitInitialized = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                param2 = it.getSerializable(ARG_PARAM2, MainActivity::class.java)
            } else {
                param2 = it.getSerializable(ARG_PARAM2) as MainActivity
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initView()
        initViewModel()
        spinLang()
        spinUnits()



        if (PrefManager.getLocationLat() == 0.0 || PrefManager.getLocationLon() == 0.0 || PrefManager.getLocationKey() == "") {
            lat = 35.6895
            lon = 139.6917
            homeWeatherViewModel.getCurrentWeather(lat, lon)
        } else {
            lat = PrefManager.getLocationLat()
            lon = PrefManager.getLocationLon()
            homeWeatherViewModel.getCurrentWeather(lat, lon)
        }


        activity?.let { locationClient = LocationServices.getFusedLocationProviderClient(it) }




        homeWeatherViewModel.currentWeatherData.observe(viewLifecycleOwner) { response ->

            when (response) {
                is ApiResponse.Loading -> {
                    showLoadingDialog()
                }

                is ApiResponse.Success -> {
                    hideLoadingDialog()
                    response.data?.let {
                        //
                        viewModel.shareData.value = it
                        binding.currentWeatherResponse = it
                        homeWeatherViewModel.getLocationKey(lat, lon)
                        val epochSeconds = it.dt.toLong()
                        val instant = Instant.ofEpochSecond(epochSeconds)
                        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                        val formatter = DateTimeFormatter.ofPattern(
                            "dd, MMM",
                            Locale(PrefManager.getCurrentLang())
                        )
                        val formattedDate = getString(R.string.today) + dateTime.format(formatter)
                        binding.tvCurrentDay.setText(formattedDate)
                        binding.tvFeelsLike.setText(
                            Math.ceil(it.main.feelsLike).toInt().toString() + "°"
                        )
                        lateinit var WindDetail: String
                        when (Math.ceil(it.wind.speed).toInt()) {
                            in 0..5 -> {
                                WindDetail = getString(R.string.very_light)
                            }

                            in 6..11 -> {
                                WindDetail = getString(R.string.moderately_light)
                            }

                            in 12..19 -> {
                                WindDetail = getString(R.string.gentle_wind)
                            }

                            in 20..28 -> {
                                WindDetail = getString(R.string.moderate_wind)
                            }

                            in 118..220 -> {
                                WindDetail = getString(R.string.strong_storm)
                            }
                        }
                        when (it.wind.deg) {
                            in 0..45 -> {
                                WindDetail += getString(R.string.from_the_north)
                            }

                            in 316..360 -> {
                                WindDetail += getString(R.string.from_the_north)
                            }

                            in 46..135 -> {
                                WindDetail += getString(R.string.from_the_east)
                            }

                            in 136..225 -> {
                                WindDetail += getString(R.string.from_the_south)
                            }

                            in 226..315 -> {
                                WindDetail += getString(R.string.from_the_west)
                            }
                        }
                        binding.tvWind.text = WindDetail

//                        xoay icon huong gio
                        binding.imgWind.rotation = it.wind.deg.toFloat()


                        when (it.main.humidity) {
                            in 0..20 -> {
                                binding.tvHumidity.text = getString(R.string.Low_dry)
                            }

                            in 21..50 -> {
                                binding.tvHumidity.text = getString(R.string.low_humidity)
                            }

                            in 51..60 -> {
                                binding.tvHumidity.text = getString(R.string.moderate_humidity)
                            }

                            in 61..80 -> {
                                binding.tvHumidity.text = getString(R.string.high_humidity)
                            }

                            in 81..100 -> {
                                binding.tvHumidity.text = getString(R.string.high_humidity_humid)
                            }
                        }

//                        hien thi phan tram do am
                        val constraintSet = ConstraintSet()
                        constraintSet.clone(binding.clHumidity)
                        constraintSet.constrainHeight(
                            binding.humidityValue.id,
                            ConstraintSet.MATCH_CONSTRAINT
                        )
//                        constraintSet.setDimensionRatio(binding.humidityValue.id, "1:1")
                        constraintSet.constrainPercentHeight(
                            binding.humidityValue.id,
                            it.main.humidity / 100f
                        )
                        constraintSet.applyTo(binding.clHumidity)


                        // hien thi phan tram ap suat
                        var presurePercent = ((it.main.pressure / 2200f) * 270f)
                        if (presurePercent <= 0) {
                            binding.myCustomView.changeAngle(0f)
                        } else if (presurePercent >= 270) {
                            binding.myCustomView.changeAngle(270f)
                        } else {
                            binding.myCustomView.changeAngle(presurePercent)
                        }

//                        may
                        when (it.clouds.all) {
                            in 0..10 -> {
                                binding.tvCloudQuality.text = getString(R.string.clear_cloudy)
                            }

                            in 11..30 -> {
                                binding.tvHumidity.text = getString(R.string.sparse_clouds)
                            }

                            in 31..60 -> {
                                binding.tvHumidity.text = getString(R.string.moderate_clouds)
                            }

                            in 61..80 -> {
                                binding.tvCloudQuality.text = getString(R.string.cloudy)
                            }

                            in 81..100 -> {
                                binding.tvCloudQuality.text = getString(R.string.cloud_dense)
                            }

                        }

                        val constraintSet3 = ConstraintSet()
                        constraintSet3.clone(binding.clCloud)
                        constraintSet3.constrainHeight(
                            binding.cloudPercent.id,
                            ConstraintSet.MATCH_CONSTRAINT
                        )
                        constraintSet3.constrainPercentHeight(
                            binding.cloudPercent.id,
                            it.clouds.all / 100f
                        )
                        constraintSet3.applyTo(binding.clCloud)
                    }
                }

                is ApiResponse.Failed -> {
                    hideLoadingDialog()
                    Toast.makeText(
                        this@HomeWeatherFragment.context,
                        getString(R.string.call_api_failed, response.message),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(TAG, "getCurrentWeather: ${response.message}")
                }
            }
        }
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
                        getString(R.string.get_location_key_failed, response.message),
                        Toast.LENGTH_SHORT
                    ).show()
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
                    mHourlyRainAdapter = HourlyRainAdapter()
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
                        mHourlyRainAdapter.updateData(it)
                        binding.rvRainyHour.apply {
                            adapter = mHourlyRainAdapter
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
                        R.string.call_api_failed.toString() + response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun spinUnits() {
        val listUnits = mutableListOf<String>()
        listUnits.addAll(resources.getStringArray(R.array.units))
        unitsAdapter = UnitsAdapter(requireContext(), R.layout.item_selected, listUnits)
        binding.spinerUnits.adapter = unitsAdapter
        binding.spinerUnits.dropDownHorizontalOffset = -80
        when (PrefManager.getCurrentUnits()) {
            "℃" -> {
                binding.spinerUnits.setSelection(0)
            }

            "℉" -> {
                binding.spinerUnits.setSelection(1)
            }

            else -> {
            }
        }
        binding.spinerUnits.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (isSpinerUnitInitialized) {
                        when (position) {
                            0 -> PrefManager.setCurrentUnits("℃")
                            1 -> PrefManager.setCurrentUnits("℉")
                            else -> PrefManager.setCurrentUnits("℃")
                        }
                        startActivity(Intent(context, MainActivity::class.java))
                        activity?.finish()
                    } else {
                        isSpinerUnitInitialized = true
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    private fun spinLang() {
        val listLang = mutableListOf<String>()
        listLang.addAll(resources.getStringArray(R.array.language))
        languageAdapter = LanguageAdapter(requireContext(), R.layout.item_selected, listLang)
        binding.spinerLanguage.adapter = languageAdapter
        when (PrefManager.getCurrentLang()) {
            "vi" -> {
                binding.spinerLanguage.setSelection(0)
            }

            "en" -> {
                binding.spinerLanguage.setSelection(1)
            }

            else -> {
            }
        }

        binding.spinerLanguage.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (isSpinerInitialized) {
                        when (position) {
                            0 -> PrefManager.setCurrentLang("vi")
                            1 -> PrefManager.setCurrentLang("en")
                            else -> PrefManager.setCurrentLang("vi")
                        }
                        startActivity(Intent(context, MainActivity::class.java))
                        activity?.finish()
                    } else {
                        isSpinerInitialized = true
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
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

                } else {
                    locationClient.lastLocation.addOnSuccessListener {
                        Log.d(TAG, "initView: lat: ${it.latitude} | long: ${it.longitude}")


                        PrefManager.setStatusLocation(true)


                        lat = it.latitude
                        lon = it.longitude

                        homeWeatherViewModel.getCurrentWeather(it.latitude, it.longitude)

                    }
                }


            }
        }

        binding.btnMenu.setOnClickListener {
            param2?.addFragment(
                WeatherFor5DaysFragment.newInstance(
                    null,
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
    ) {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!PrefManager.getStatusLocation()) {
                showAlertDialog()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {

                    PrefManager.setStatusLocation(false)

                } else {
                    return@registerForActivityResult
                }
            }
        } else locationClient.lastLocation.addOnSuccessListener {
            Log.d("TAG", ": lat: ${it.latitude} | long: ${it.longitude}")

            PrefManager.setStatusLocation(true)


            lat = it.latitude
            lon = it.longitude

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
                dialog.dismiss()
            }
        }
        val btnView2 = dialog?.findViewById<Button>(R.id.btnSetting)
        if (btnView2 != null) {
            btnView2.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", "com.lampro.myaccuweather", null)
                intent.data = uri

                startActivity(intent)

                dialog.dismiss()
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