package com.lampro.myaccuweather.network.api.request

import com.lampro.myaccuweather.constants.ConstantsApi
import com.lampro.myaccuweather.objects.hourlyweatherresponse.HourlyWeatherResponse
import com.lampro.myaccuweather.objects.infomationcityreponse.InfCityResponse
import com.lampro.myaccuweather.objects.locationkeyresponse.LocationKeyResponse
import com.lampro.myaccuweather.objects.uvindexresponse.UVIndexResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IAccuWeatherService {

    @GET(ConstantsApi.GET_HOURLY_WEATHER)
    suspend fun getHourlyWeather(@Path("locationkey") locationKey: String): Response<HourlyWeatherResponse>

    @GET(ConstantsApi.GET_INF_BY_CITY_NAME)
    suspend fun getInfCityByName(@Query("q") cityName: String): Response<InfCityResponse>

    @GET(ConstantsApi.GET_UV_INDEX)
    suspend fun getUvIndex(@Path("locationkey") loactionKey: String): Response<UVIndexResponse>
    @GET(ConstantsApi.GET_LOCATION_KEY)
    suspend fun getLocationKey(@Query("q") q: String): Response<LocationKeyResponse>
}
