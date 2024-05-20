package com.lampro.myaccuweather.objects.currentweatherresponse


import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("Direction")
    var direction: Direction,
    @SerializedName("Speed")
    var speed: Speed
)