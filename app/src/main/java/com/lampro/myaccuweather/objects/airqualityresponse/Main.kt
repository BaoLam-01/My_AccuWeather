package com.lampro.myaccuweather.objects.airqualityresponse


import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("aqi")
    var aqi: Int
)