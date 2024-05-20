package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class Temperature(
    @SerializedName("Maximum")
    var maximum: MaximumXXXXXX,
    @SerializedName("Minimum")
    var minimum: Minimum
)