package com.lampro.myaccuweather.repositories

import com.lampro.myaccuweather.objects.hourlyweatherresponse.HourlyWeatherResponse
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.network.api.GenericApiResponse
import com.lampro.myaccuweather.network.api.clients.AccuClient
import com.lampro.myaccuweather.network.api.clients.OpenClient
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponse
import com.lampro.myaccuweather.objects.infomationcityreponse.InfCityResponse
import com.lampro.myaccuweather.objects.locationkeyresponse.LocationKeyResponse

class HomeWeatherRepository : GenericApiResponse() {
    suspend fun getCurrentWeather(q: String): ApiResponse<CurrentWeatherResponse> {
        return apiCall { OpenClient.getOpenWeatherApi.getCurrentWeather(q) }
    }

    suspend fun getCurrentWeather(lat: Double, lon: Double): ApiResponse<CurrentWeatherResponse> {
        return apiCall {
            OpenClient.getOpenWeatherApi.getCurrentWeather(
                lat.toString(),
                lon.toString()
            )
        }
    }

    suspend fun getHourlyWeather(locationKey: String): ApiResponse<HourlyWeatherResponse> {
        return apiCall { AccuClient.getAccuWeatherApi.getHourlyWeather(locationKey) }
    }

    suspend fun getInfCityByName(cityName: String): ApiResponse<InfCityResponse> {
        return apiCall { AccuClient.getAccuWeatherApi.getInfCityByName(cityName) }
    }
    suspend fun getLocationKey(lat : Double, lon: Double) : ApiResponse<LocationKeyResponse>{
        return apiCall {
            AccuClient.getAccuWeatherApi.getLocationKey("$lat,$lon")
        }
    }
}