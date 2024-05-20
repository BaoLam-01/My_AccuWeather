package com.lampro.myaccuweather.objects.locationkeyresponse


import com.google.gson.annotations.SerializedName

data class GeoPosition(
    @SerializedName("Elevation")
    var elevation: Elevation,
    @SerializedName("Latitude")
    var latitude: Double,
    @SerializedName("Longitude")
    var longitude: Double
)