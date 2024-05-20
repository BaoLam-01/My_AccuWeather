package com.lampro.myaccuweather.objects.hourlyweatherresponse


import com.google.gson.annotations.SerializedName

data class Direction(
    @SerializedName("Degrees")
    var degrees: Int,
    @SerializedName("English")
    var english: String,
    @SerializedName("Localized")
    var localized: String
)