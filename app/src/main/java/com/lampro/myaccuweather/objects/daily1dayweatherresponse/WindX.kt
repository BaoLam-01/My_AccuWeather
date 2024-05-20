package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class WindX(
    @SerializedName("Direction")
    var direction: DirectionXX,
    @SerializedName("Speed")
    var speed: SpeedXX
)