package com.lampro.myaccuweather.objects.hourlyweatherresponse


import com.google.gson.annotations.SerializedName

data class TotalLiquid(
    @SerializedName("Unit")
    var unit: String,
    @SerializedName("UnitType")
    var unitType: Int,
    @SerializedName("Value")
    var value: Double
)