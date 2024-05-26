package com.lampro.myaccuweather.objects.geopositionresponse


import com.google.gson.annotations.SerializedName

data class GeopositionResponseItem(
    @SerializedName("country")
    var country: String,
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("local_names")
    var localNames: LocalNames,
    @SerializedName("lon")
    var lon: Double,
    @SerializedName("name")
    var name: String
)