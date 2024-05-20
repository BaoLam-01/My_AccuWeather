package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class WetBulbTemperature(
    @SerializedName("Average")
    var average: AverageX,
    @SerializedName("Maximum")
    var maximum: MaximumX,
    @SerializedName("Minimum")
    var minimum: Minimum
)