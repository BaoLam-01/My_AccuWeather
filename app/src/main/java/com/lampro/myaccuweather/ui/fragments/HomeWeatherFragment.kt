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
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lampro.myaccuweather.R
import com.lampro.myaccuweather.adapters.HourlyWeatherAdapter
import com.lampro.myaccuweather.adapters.LanguageAdapter
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
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var languageAdapter: LanguageAdapter
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
        val listLang = mutableListOf<String>()
        listLang.addAll(resources.getStringArray(R.array.language))
        languageAdapter = LanguageAdapter(requireContext(),R.layout.item_selected,listLang)
        binding.spinerLanguage.adapter = languageAdapter


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
                        viewModel.shareData.value = it
                        binding.currentWeatherResponse = it
                        val epochSeconds = it.dt.toLong()
                        val instant = Instant.ofEpochSecond(epochSeconds)
                        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                        val formatter = DateTimeFormatter.ofPattern("dd, MMM")
                        val formattedDate = "Hôm nay: " + dateTime.format(formatter)
                        binding.tvCurrentDay.setText(formattedDate)
                        binding.tvFeelsLike.setText(
                            Math.ceil(it.main.feelsLike).toInt().toString() + "°"
                        )
                        lateinit var WindDetail: String
                        when (Math.ceil(it.wind.speed).toInt()) {
                            in 0..5 -> {
                                WindDetail = "Gió rất nhẹ"
                            }

                            in 6..11 -> {
                                WindDetail = "Gió nhẹ vừa phải"
                            }

                            in 12..19 -> {
                                WindDetail = "Gió nhẹ nhàng"
                            }

                            in 20..28 -> {
                                WindDetail = "Gió vừa phải"
                            }

                            in 118..220 -> {
                                WindDetail = "Gió bão cực mạnh"
                            }
                        }
                        when (it.wind.deg) {
                            in 0..45 -> {
                                WindDetail += ", Từ hướng bắc"
                            }

                            in 316..360 -> {
                                WindDetail += ", Từ hướng bắc"
                            }

                            in 46..135 -> {
                                WindDetail += ", Từ hướng đông"
                            }

                            in 136..225 -> {
                                WindDetail += ", Từ hướng nam"
                            }

                            in 226..315 -> {
                                WindDetail += ", Từ hướng tây"
                            }
                        }
                        binding.tvWind.text = WindDetail

//                        xoay icon huong gio
                        binding.imgWind.rotation = it.wind.deg.toFloat()


                        when (it.main.humidity) {
                            in 0..20 -> {
                                binding.tvHumidity.text = "Độ ẩm thấp, thời tiết hanh khô"
                            }

                            in 21..50 -> {
                                binding.tvHumidity.text = "Độ ẩm thấp"
                            }

                            in 51..60 -> {
                                binding.tvHumidity.text = "Độ ẩm vừa phải"
                            }

                            in 61..80 -> {
                                binding.tvHumidity.text = "Độ ẩm cao"
                            }

                            in 81..100 -> {
                                binding.tvHumidity.text = "Độ ẩm cao, trời nồm"
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
                        }else if (presurePercent >= 270) {
                            binding.myCustomView.changeAngle(270f)
                        } else {
                            binding.myCustomView.changeAngle(presurePercent)
                        }

//                        may
                        when (it.clouds.all) {
                            in 0..10 -> {
                                binding.tvCloudQuality.text = "Trời quang mây"
                            }

                            in 11..30 -> {
                                binding.tvHumidity.text = "Mây thưa thớt"
                            }

                            in 31..60 -> {
                                binding.tvHumidity.text = "Lượng mây vưa phải"
                            }

                            in 61..80 -> {
                                binding.tvCloudQuality.text = "Trời nhiều mây"
                            }

                            in 81..100 -> {
                                binding.tvCloudQuality.text = "Mật độ mây dày đặc"
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
        val btnView2 = dialog?.findViewById<Button>(R.id.btnSetting)
        if (btnView2 != null) {
            btnView2.setOnClickListener {
                if (dialog != null) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", "com.lampro.myaccuweather", null)
                    intent.data = uri

                    startActivity(intent)

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