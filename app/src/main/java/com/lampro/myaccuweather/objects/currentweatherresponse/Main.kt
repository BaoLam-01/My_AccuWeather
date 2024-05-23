package com.lampro.myaccuweather.objects.currentweatherresponse


import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("feels_like")
    var feelsLike: Double,
    @SerializedName("grnd_level")
    var grndLevel: Int,
    @SerializedName("humidity")
    var humidity: Int,
    @SerializedName("pressure")
    var pressure: Int,
    @SerializedName("sea_level")
    var seaLevel: Int,
    @SerializedName("temp")
    var temp: Int,
    @SerializedName("temp_max")
    var tempMax: Int,
    @SerializedName("temp_min")
    var tempMin: Int
)