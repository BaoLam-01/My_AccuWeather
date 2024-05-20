package com.lampro.myaccuweather.objects.dailyweatherresponse


import com.google.gson.annotations.SerializedName

data class DailyForecast(
    @SerializedName("Date")
    var date: String,
    @SerializedName("Day")
    var day: Day,
    @SerializedName("EpochDate")
    var epochDate: Int,
    @SerializedName("Link")
    var link: String,
    @SerializedName("MobileLink")
    var mobileLink: String,
    @SerializedName("Night")
    var night: Night,
    @SerializedName("Sources")
    var sources: List<String>,
    @SerializedName("Temperature")
    var temperature: Temperature
)