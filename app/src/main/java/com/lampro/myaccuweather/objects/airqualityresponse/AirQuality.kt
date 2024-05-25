package com.lampro.myaccuweather.objects.airqualityresponse


import com.google.gson.annotations.SerializedName

data class AirQuality (
    @SerializedName("components")
    var components: Components,
    @SerializedName("dt")
    var dt: Int,
    @SerializedName("main")
    var main: Main
)