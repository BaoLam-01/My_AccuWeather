package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class WetBulbTemperature(
    @SerializedName("Average")
    var average: Average,
    @SerializedName("Maximum")
    var maximum: Maximum,
    @SerializedName("Minimum")
    var minimum: Minimum
)