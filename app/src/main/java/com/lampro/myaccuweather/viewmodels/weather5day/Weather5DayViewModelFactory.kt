package com.lampro.myaccuweather.viewmodels.weather5day

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lampro.myaccuweather.repositories.Weather5DayResponsitory

class Weather5DayViewModelFactory(
    val application: Application,
    val weather5DayResponsitory: Weather5DayResponsitory
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return Weather5DayViewModel(application,weather5DayResponsitory) as T
    }
}