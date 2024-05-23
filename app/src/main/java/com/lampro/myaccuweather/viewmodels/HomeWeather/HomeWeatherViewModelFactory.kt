package com.lampro.myaccuweather.viewmodels.HomeWeather

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lampro.myaccuweather.repositories.WeatherRepository

class HomeWeatherViewModelFactory(
    val application: Application,
    val weatherRepository: WeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeWeatherViewModel(application,weatherRepository) as  T
    }
}