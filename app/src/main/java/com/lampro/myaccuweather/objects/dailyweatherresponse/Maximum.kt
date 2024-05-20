package com.lampro.myaccuweather.objects.dailyweatherresponse


import com.google.gson.annotations.SerializedName

data class Maximum(
    @SerializedName("Unit")
    var unit: String,
    @SerializedName("UnitType")
    var unitType: Int,
    @SerializedName("Value")
    var value: Double
)