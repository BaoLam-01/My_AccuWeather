package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class RealFeelTemperatureShade(
    @SerializedName("Maximum")
    var maximum: MaximumXXXXX,
    @SerializedName("Minimum")
    var minimum: MinimumXXXXX
)