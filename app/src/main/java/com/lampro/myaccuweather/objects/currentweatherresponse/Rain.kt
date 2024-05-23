package com.lampro.myaccuweather.objects.currentweatherresponse


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    var h: Double
)