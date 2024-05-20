package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class WindGust(
    @SerializedName("Direction")
    var direction: Direction,
    @SerializedName("Speed")
    var speed: Speed
)