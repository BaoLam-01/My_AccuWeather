package com.lampro.myaccuweather.viewmodels.HomeWeather

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponse
import com.lampro.myaccuweather.objects.daily1dayweatherresponse.Daily1DayWeatherResponse
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeatherResponse
import com.lampro.myaccuweather.objects.hourlyweatherresponse.HourlyWeatherResponse
import com.lampro.myaccuweather.objects.infomationcityreponse.InfCityResponseItem
import com.lampro.myaccuweather.constants.ConstantsApi
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.repositories.WeatherRepository
import kotlinx.coroutines.launch

class HomeWeatherViewModel(application: Application, val weatherRepository: WeatherRepository) :
    AndroidViewModel(application) {
    val currentWeatherData = MutableLiveData<ApiResponse<CurrentWeatherResponse>>()
    val hourlyWeatherData = MutableLiveData<ApiResponse<HourlyWeatherResponse>>()
    val locationKeyData = MutableLiveData<String?>()
    val cityNameData= MutableLiveData<String?>()
    val dailyWeatherData = MutableLiveData<ApiResponse<DailyWeatherResponse>>()
    val _1DayWeatherData = MutableLiveData<ApiResponse<Daily1DayWeatherResponse>>()
    val infCity = MutableLiveData<InfCityResponseItem>()
    val requestQueue: RequestQueue = Volley.newRequestQueue(application)

    init {
//        getCurrentWeather()
//        getHourlyWeather()
    }

    fun getLocationKey(latitude: Double, longitude: Double){
        val url: String =
            "https://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=${ConstantsApi.API_KEY}&q=$latitude%2C$longitude&language=vi"
        val objectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            locationKeyData.value = response.getString("Key")
        }, { error ->
            locationKeyData.value = null
            Log.d(TAG, "getLocationKey: $error")
        })
        requestQueue.add(objectRequest)
    }
    fun getCityName(key: String){
        val url: String =
            "https://dataservice.accuweather.com/locations/v1/cities/neighbors/$key?apikey=${ConstantsApi.API_KEY}&language=vi&details=false"
        val arrayRequest = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            var jsonobject = response.getJSONObject(0)
            if (jsonobject.getString("LocalizedName").isNotEmpty()){
                Log.d(TAG, "getCityName: $jsonobject")
                cityNameData.value = jsonobject.getString("LocalizedName")
            }else {
                cityNameData.value = jsonobject.getString("EnglishName")
            }
        }, { error ->
            cityNameData.value = null
            Log.d(TAG, "getLocationKey: $error")
        })
        requestQueue.add(arrayRequest)
    }
    fun getInfByCityName(cityName: String){
        val url: String =
            "https://dataservice.accuweather.com/locations/v1/cities/search?apikey=${ConstantsApi.API_KEY}&q=$cityName&language=vi&details=true&offset=3"
        val arrayRequest = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            var jsonobject = response.getJSONObject(0).toString()
            Log.d(TAG, "getInfByCityName: $jsonobject")
        }, { error ->
            Log.d(TAG, "getLocationKey: $error")
        })
        requestQueue.add(arrayRequest)
    }


    fun getCurrentWeather(locaitonKey: String = "353412") {
        currentWeatherData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weatherRepository.getCurrentWeather(locaitonKey)
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
    fun getDailyWeather(locationKey: String = "353412") {
        dailyWeatherData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weatherRepository.getDailyWeather(locationKey)
            dailyWeatherData.value = response
        }
    }
    fun getDaily1DayWeather(locationKey: String = "353412") {
        _1DayWeatherData.value = ApiResponse.Loading()
        viewModelScope.launch {
            val response = weatherRepository.getDaily1DayWeather(locationKey)
            _1DayWeatherData.value = response
        }
    }

}