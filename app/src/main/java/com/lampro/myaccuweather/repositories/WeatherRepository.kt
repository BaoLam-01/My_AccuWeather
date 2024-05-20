package com.lampro.weatherapp.repositories

import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponse
import com.lampro.myaccuweather.objects.daily1dayweatherresponse.Daily1DayWeatherResponse
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeatherResponse
import com.lampro.myaccuweather.objects.hourlyweatherresponse.HourlyWeatherResponse
import com.lampro.weatherapp.network.api.ApiResponse
import com.lampro.weatherapp.network.api.GenericApiResponse
import com.lampro.weatherapp.network.api.RetrofitClient

class WeatherRepository : GenericApiResponse(){
    suspend fun getCurrentWeather(locationKey: String): ApiResponse<CurrentWeatherResponse> {
        return apiCall { RetrofitClient.getWeatherApi.getCurrentWeather(locationKey) }
    }
    suspend fun getHourlyWeather(locationKey: String): ApiResponse<HourlyWeatherResponse> {
        return apiCall { RetrofitClient.getWeatherApi.getHourlyWeather(locationKey) }
    }
    suspend fun getDailyWeather(locationKey: String): ApiResponse<DailyWeatherResponse> {
        return apiCall { RetrofitClient.getWeatherApi.getDailyWeather(locationKey) }
    }
    suspend fun getDaily1DayWeather(locationKey: String): ApiResponse<Daily1DayWeatherResponse> {
        return apiCall { RetrofitClient.getWeatherApi.getDaily1DayWeather(locationKey) }
    }
}