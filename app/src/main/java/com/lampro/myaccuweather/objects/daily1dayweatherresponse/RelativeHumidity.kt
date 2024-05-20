package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class RelativeHumidity(
    @SerializedName("Average")
    var average: Int,
    @SerializedName("Maximum")
    var maximum: Int,
    @SerializedName("Minimum")
    var minimum: Int
)