package com.lampro.myaccuweather.objects.currentweatherresponse


import com.google.gson.annotations.SerializedName

data class WindGust(
    @SerializedName("Speed")
    var speed: Speed
)