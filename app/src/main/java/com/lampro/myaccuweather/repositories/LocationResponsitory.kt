package com.lampro.myaccuweather.repositories

import com.google.android.gms.common.api.Api
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.network.api.GenericApiResponse
import com.lampro.myaccuweather.network.api.clients.AccuClient
import com.lampro.myaccuweather.network.api.clients.OpenClient
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeatherResponse
import com.lampro.myaccuweather.objects.geopositionresponse.GeopositionResponse
import com.lampro.myaccuweather.objects.locationkeyresponse.LocationKeyResponse

class LocationResponsitory : GenericApiResponse() {
    suspend fun getLocationKey(lat : Double, lon: Double) : ApiResponse<LocationKeyResponse>{
        return apiCall {
            AccuClient.getAccuWeatherApi.getLocationKey("$lat,$lon")
        }
    }
    suspend fun getGeoByCityName(cityName:String) : ApiResponse<GeopositionResponse>{
        return apiCall {
            OpenClient.getOpenWeatherApi.getGeoByCityName(cityName)
        }
    }

}
