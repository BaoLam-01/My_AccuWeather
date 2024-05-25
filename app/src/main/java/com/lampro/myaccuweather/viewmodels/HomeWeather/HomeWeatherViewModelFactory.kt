package com.lampro.myaccuweather.viewmodels.HomeWeather

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lampro.myaccuweather.repositories.HomeWeatherRepository

class HomeWeatherViewModelFactory(
    val application: Application,
    val weatherRepository: HomeWeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeWeatherViewModel(application,weatherRepository) as  T
    }
}