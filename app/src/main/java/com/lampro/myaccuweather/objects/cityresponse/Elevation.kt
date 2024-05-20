package com.lampro.myaccuweather.objects.cityresponse


import com.google.gson.annotations.SerializedName

data class Elevation(
    @SerializedName("Imperial")
    var imperial: Imperial,
    @SerializedName("Metric")
    var metric: Metric
)