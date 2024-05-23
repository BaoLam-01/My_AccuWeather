package com.lampro.myaccuweather.network.api.request

import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponse
import com.lampro.myaccuweather.objects.daily1dayweatherresponse.Daily1DayWeatherResponse
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeatherResponse
import com.lampro.myaccuweather.objects.hourlyweatherresponse.HourlyWeatherResponse
import com.lampro.myaccuweather.constants.ConstantsApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IWeatherService {
    @GET(ConstantsApi.GET_CURRENT_WEATHER)
    suspend fun getCurrentWeather(@Path("locationkey") locationKey: String): Response<CurrentWeatherResponse>

    @GET(ConstantsApi.GET_HOURLY_WEATHER)
    suspend fun getHourlyWeather(@Path("locationkey") locationKey: String): Response<HourlyWeatherResponse>
    @GET(ConstantsApi.GET_DAILY_WEATHER)
    suspend fun getDailyWeather(@Path("locationkey") locationKey: String): Response<DailyWeatherResponse>
    @GET(ConstantsApi.GET_DAILY_1DAY_WEATHER)
    suspend fun getDaily1DayWeather(@Path("locationkey") locationKey: String): Response<Daily1DayWeatherResponse>
}
