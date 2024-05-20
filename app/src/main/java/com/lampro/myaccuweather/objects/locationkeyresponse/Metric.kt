package com.lampro.myaccuweather.objects.locationkeyresponse


import com.google.gson.annotations.SerializedName

data class Metric(
    @SerializedName("Unit")
    var unit: String,
    @SerializedName("UnitType")
    var unitType: Int,
    @SerializedName("Value")
    var value: Int
)