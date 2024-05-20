package com.lampro.myaccuweather.objects.dailyweatherresponse


import com.google.gson.annotations.SerializedName

data class DailyWeatherResponse(
    @SerializedName("DailyForecasts")
    var dailyForecasts: List<DailyForecast>,
    @SerializedName("Headline")
    var headline: Headline
)