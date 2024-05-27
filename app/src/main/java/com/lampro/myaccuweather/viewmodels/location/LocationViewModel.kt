package com.lampro.myaccuweather.viewmodels.location

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lampro.myaccuweather.MyApplication
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponse
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeatherResponse
import com.lampro.myaccuweather.objects.geopositionresponse.GeopositionResponse
import com.lampro.myaccuweather.objects.locationkeyresponse.LocationKeyResponse
import com.lampro.myaccuweather.repositories.LocationResponsitory
import kotlinx.coroutines.launch

class LocationViewModel(application: Application, val locationResponsitory: LocationResponsitory): AndroidViewModel(application){
    val locationKeyData = MutableLiveData<ApiResponse<LocationKeyResponse>>()
    val geoByCityNameData = MutableLiveData<ApiResponse<GeopositionResponse>>()
    val currentWeatherData = MutableLiveData<ApiResponse<CurrentWeatherResponse>>()
    fun getLocationKey(latitude: Double, longitude: Double) {
        locationKeyData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = locationResponsitory.getLocationKey(latitude, longitude)
            locationKeyData.value = response
        }
    }
    fun getGeoByCityName(cityName: String){
        geoByCityNameData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = locationResponsitory.getGeoByCityName(cityName)
            geoByCityNameData.value = response
        }
    }
    fun getCurrentWeather(lat: Double, lon: Double) {
        currentWeatherData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = locationResponsitory.getCurrentWeather(lat, lon)
            currentWeatherData.value = response
        }
    }
}