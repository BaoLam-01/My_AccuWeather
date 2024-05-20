package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class WetBulbGlobeTemperatureX(
    @SerializedName("Average")
    var average: AverageXX,
    @SerializedName("Maximum")
    var maximum: MaximumXX,
    @SerializedName("Minimum")
    var minimum: Minimum
)