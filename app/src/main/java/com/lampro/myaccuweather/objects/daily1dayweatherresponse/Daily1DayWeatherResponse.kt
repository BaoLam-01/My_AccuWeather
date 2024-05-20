package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class Daily1DayWeatherResponse(
    @SerializedName("DailyForecasts")
    var dailyForecasts: List<DailyForecast>,
    @SerializedName("Headline")
    var headline: Headline
)