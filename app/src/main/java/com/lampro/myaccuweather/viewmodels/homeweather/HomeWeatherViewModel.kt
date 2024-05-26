package com.lampro.myaccuweather.viewmodels.homeweather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponse
import com.lampro.myaccuweather.objects.hourlyweatherresponse.HourlyWeatherResponse
import com.lampro.myaccuweather.objects.infomationcityreponse.InfCityResponse
import com.lampro.myaccuweather.objects.locationkeyresponse.LocationKeyResponse
import com.lampro.myaccuweather.repositories.HomeWeatherRepository
import kotlinx.coroutines.launch

class HomeWeatherViewModel(application: Application, val weatherRepository: HomeWeatherRepository) :
    AndroidViewModel(application) {
    val currentWeatherData = MutableLiveData<ApiResponse<CurrentWeatherResponse>>()
    val hourlyWeatherData = MutableLiveData<ApiResponse<HourlyWeatherResponse>>()
    val locationKeyData = MutableLiveData<ApiResponse<LocationKeyResponse>>()
    val cityNameData = MutableLiveData<String?>()
    val infCity = MutableLiveData<ApiResponse<InfCityResponse>>()
    val requestQueue: RequestQueue = Volley.newRequestQueue(application)

    init {
//        getCurrentWeather()
//        getHourlyWeather()
    }

    fun getLocationKey(latitude: Double, longitude: Double) {
        locationKeyData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weatherRepository.getLocationKey(latitude, longitude)
            locationKeyData.value = response
        }
    }

    fun getInfByCityName(cityName: String) {
        infCity.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weatherRepository.getInfCityByName(cityName)
            infCity.value = response
        }
    }


    fun getCurrentWeather(q: String = "hanoi") {
        currentWeatherData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weatherRepository.getCurrentWeather(q)
            currentWeatherData.value = response
        }
    }

    fun getCurrentWeather(lat: Double, lon: Double) {
        currentWeatherData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weatherRepository.getCurrentWeather(lat, lon)
            currentWeatherData.value = response
        }
    }

    fun getHourlyWeather(locationKey: String = "353412") {
        hourlyWeatherData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weatherRepository.getHourlyWeather(locationKey)
            hourlyWeatherData.value = response
        }
    }


}