package com.lampro.myaccuweather.objects.hourlyweatherresponse


import com.google.gson.annotations.SerializedName

data class WindGust(
    @SerializedName("Speed")
    var speed: Speed
)