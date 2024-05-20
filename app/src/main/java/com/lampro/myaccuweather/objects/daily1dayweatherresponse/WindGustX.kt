package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class WindGustX(
    @SerializedName("Direction")
    var direction: DirectionXXX,
    @SerializedName("Speed")
    var speed: SpeedXXX
)