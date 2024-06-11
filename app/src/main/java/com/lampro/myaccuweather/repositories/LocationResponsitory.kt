package com.lampro.myaccuweather.repositories

import com.google.android.gms.common.api.Api
import com.lampro.myaccuweather.network.api.ApiResponse
import com.lampro.myaccuweather.network.api.GenericApiResponse
import com.lampro.myaccuweather.network.api.clients.AccuClient
import com.lampro.myaccuweather.network.api.clients.OpenClient
import com.lampro.myaccuweather.objects.currentweatherresponse.CurrentWeatherResponse
import com.lampro.myaccuweather.objects.dailyweatherresponse.DailyWeatherResponse
import com.lampro.myaccuweather.objects.geopositionresponse.GeopositionResponse
import com.lampro.myaccuweather.objects.locationkeyresponse.LocationKeyResponse
import com.lampro.myaccuweather.utils.PrefManager

class LocationResponsitory : GenericApiResponse() {
    val lang = PrefManager.getCurrentLang()
    val units = if (PrefManager.getCurrentUnits() == "℃") {
        "metric"
    } else {
        "imperial"
    }

    suspend fun getLocationKey(lat: Double, lon: Double): ApiResponse<LocationKeyResponse> {
        return apiCall {
            AccuClient.getAccuWeatherApi.getLocationKey("$lat,$lon", lang)
        }
    }

    suspend fun getGeoByCityName(cityName: String): ApiResponse<GeopositionResponse> {
        return apiCall {
            OpenClient.getOpenWeatherApi.getGeoByCityName(cityName, lang, units)
        }
    }

    suspend fun getCurrentWeather(lat: Double, lon: Double): ApiResponse<CurrentWeatherResponse> {
        return apiCall {
            OpenClient.getOpenWeatherApi.getCurrentWeather(
                lat.toString(),
                lon.toString(),
                lang,
                units

            )
        }
    }

}
