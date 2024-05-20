package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class RealFeelTemperatureShade(
    @SerializedName("Maximum")
    var maximum: MaximumXXXX,
    @SerializedName("Minimum")
    var minimum: MinimumXXXX
)