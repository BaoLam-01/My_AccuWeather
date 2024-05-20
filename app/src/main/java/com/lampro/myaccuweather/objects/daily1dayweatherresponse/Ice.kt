package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class Ice(
    @SerializedName("Unit")
    var unit: String,
    @SerializedName("UnitType")
    var unitType: Int,
    @SerializedName("Value")
    var value: Int
)