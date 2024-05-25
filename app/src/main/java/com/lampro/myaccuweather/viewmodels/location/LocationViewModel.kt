package com.lampro.myaccuweather.viewmodels.location

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lampro.myaccuweather.MyApplication
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeatherResponse
import com.lampro.myaccuweather.repositories.LocationResponsitory
import kotlinx.coroutines.launch

class LocationViewModel(application: MyApplication, val locationResponsitory: LocationResponsitory): AndroidViewModel(application){

}