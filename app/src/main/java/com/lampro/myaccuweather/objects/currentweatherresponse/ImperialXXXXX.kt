package com.lampro.myaccuweather.objects.currentweatherresponse


import com.google.gson.annotations.SerializedName

data class ImperialXXXXX(
    @SerializedName("Unit")
    var unit: String,
    @SerializedName("UnitType")
    var unitType: Int,
    @SerializedName("Value")
    var value: Double
)