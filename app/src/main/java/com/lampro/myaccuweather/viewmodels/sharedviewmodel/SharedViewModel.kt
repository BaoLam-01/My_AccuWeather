package com.lampro.myaccuweather.viewmodels.sharedviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponse

class SharedViewModel: ViewModel(){
     val shareData =  MutableLiveData<CurrentWeatherResponse>(null)
}