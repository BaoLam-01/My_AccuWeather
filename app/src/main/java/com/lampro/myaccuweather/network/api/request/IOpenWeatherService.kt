package com.lampro.myaccuweather.network.api.request

import com.lampro.myaccuweather.constants.ConstantsApi
import com.lampro.myaccuweather.objects.airqualityresponse.AirQualityResponse
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponse
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IOpenWeatherService {
    @GET(ConstantsApi.GET_CURRENT_WEATHER)
    suspend fun getCurrentWeather(@Query("q") q: String): Response<CurrentWeatherResponse>

    @GET(ConstantsApi.GET_CURRENT_WEATHER)
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<CurrentWeatherResponse>

    @GET(ConstantsApi.GET_DAILY_WEATHER)
    suspend fun getDailyWeather(@Query("q") q: String): Response<DailyWeatherResponse>

    @GET(ConstantsApi.GET_DAILY_WEATHER)
    suspend fun getDailyWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<DailyWeatherResponse>
    @GET(ConstantsApi.GET_AIR_QUALITY)
    suspend fun getAirQuality(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<AirQualityResponse>
}
