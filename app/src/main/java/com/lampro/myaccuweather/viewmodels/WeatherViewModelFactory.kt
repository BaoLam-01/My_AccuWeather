package com.lampro.weatherapp.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lampro.weatherapp.repositories.WeatherRepository
import org.w3c.dom.Comment

class WeatherViewModelFactory(
    val application: Application,
    val weatherRepository: WeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(application,weatherRepository) as  T
    }
}