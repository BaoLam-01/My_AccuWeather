package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class Minimum(
    @SerializedName("Unit")
    var unit: String,
    @SerializedName("UnitType")
    var unitType: Int,
    @SerializedName("Value")
    var value: Double
)