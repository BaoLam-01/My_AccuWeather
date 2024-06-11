package com.lampro.myaccuweather.network.api.request

import com.lampro.myaccuweather.constants.ConstantsApi
import com.lampro.myaccuweather.objects.airqualityresponse.AirQualityResponse
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponse
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeatherResponse
import com.lampro.myaccuweather.objects.geopositionresponse.GeopositionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IOpenWeatherService {
    @GET(ConstantsApi.GET_CURRENT_WEATHER)
    suspend fun getCurrentWeather(
        @Query("q") q: String,
        @Query("lang") lang: String
    ): Response<CurrentWeatherResponse>

    @GET(ConstantsApi.GET_CURRENT_WEATHER)
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String
    ): Response<CurrentWeatherResponse>

    @GET(ConstantsApi.GET_DAILY_WEATHER)
    suspend fun getDailyWeather(
        @Query("q") q: String,
        @Query("lang") lang: String
    ): Response<DailyWeatherResponse>

    @GET(ConstantsApi.GET_DAILY_WEATHER)
    suspend fun getDailyWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String
    ): Response<DailyWeatherResponse>

    @GET(ConstantsApi.GET_AIR_QUALITY)
    suspend fun getAirQuality(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
    ): Response<AirQualityResponse>

    @GET(ConstantsApi.GET_GEO_BY_CITY_NAME)
    suspend fun getGeoByCityName(
        @Query("q") q: String,
        @Query("lang") lang: String
    ): Response<GeopositionResponse>
}

