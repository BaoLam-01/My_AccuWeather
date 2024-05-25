package com.lampro.myaccuweather.viewmodels.weather5day

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lampro.myaccuweather.MyApplication
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.objects.airqualityresponse.AirQualityResponse
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeatherResponse
import com.lampro.myaccuweather.objects.locationkeyresponse.LocationKeyResponse
import com.lampro.myaccuweather.objects.uvindexresponse.UVIndexResponse
import com.lampro.myaccuweather.repositories.Weather5DayResponsitory
import kotlinx.coroutines.launch

class Weather5DayViewModel(
    application: Application,
    val weather5DayResponsitory: Weather5DayResponsitory
) : AndroidViewModel(application) {
    val dailyWeatherData = MutableLiveData<ApiResponse<DailyWeatherResponse>>()
    val airQualityData = MutableLiveData<ApiResponse<AirQualityResponse>>()
    val uvIndexData = MutableLiveData<ApiResponse<UVIndexResponse>>()
    val locationKeyData = MutableLiveData<ApiResponse<LocationKeyResponse>>()
    fun getDailyWeather(cityName: String) {
        dailyWeatherData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weather5DayResponsitory.getDailyWeather(cityName)
            dailyWeatherData.value = response
        }
    }

    fun getDailyWeather(lat: Double, lon: Double) {
        dailyWeatherData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weather5DayResponsitory.getDailyWeather(lat, lon)
            dailyWeatherData.value = response
        }
    }

    fun getAirQuality(lat: Double, lon: Double) {
        airQualityData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weather5DayResponsitory.getAirQuality(lat, lon)
            airQualityData.value = response
        }
    }

    fun getUvIndex(locationKey: String) {
        uvIndexData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weather5DayResponsitory.getUvIndex(locationKey)
            uvIndexData.value = response
        }
    }

    fun getLocationKey(lat: Double, lon: Double) {
        locationKeyData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weather5DayResponsitory.getLocationKey(lat, lon)
            locationKeyData.value = response
        }
    }


}