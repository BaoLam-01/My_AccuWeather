package com.lampro.myaccuweather.objects.airqualityresponse


import com.google.gson.annotations.SerializedName

data class AirQualityResponse(
    @SerializedName("coord")
    var coord: Coord,
    @SerializedName("list")
    var list: List<AirQuality>
)