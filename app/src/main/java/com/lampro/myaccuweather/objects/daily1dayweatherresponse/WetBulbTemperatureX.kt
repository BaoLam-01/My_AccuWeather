package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class WetBulbTemperatureX(
    @SerializedName("Average")
    var average: AverageXXX,
    @SerializedName("Maximum")
    var maximum: MaximumXXX,
    @SerializedName("Minimum")
    var minimum: Minimum
)