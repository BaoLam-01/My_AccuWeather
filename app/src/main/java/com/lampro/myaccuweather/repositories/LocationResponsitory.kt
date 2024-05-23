package com.lampro.myaccuweather.repositories

import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.network.api.GenericApiResponse
import com.lampro.myaccuweather.network.api.RetrofitClient
import com.lampro.myaccuweather.objects.daily1dayweatherresponse.Daily1DayWeatherResponse

class LocationResponsitory: GenericApiResponse() {
    suspend fun getDaily1DayWeather(locationKey: String): ApiResponse<Daily1DayWeatherResponse> {
        return apiCall { RetrofitClient.getWeatherApi.getDaily1DayWeather(locationKey) }
    }
}