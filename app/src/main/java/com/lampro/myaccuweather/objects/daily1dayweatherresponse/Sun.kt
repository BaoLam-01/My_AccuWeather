package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class Sun(
    @SerializedName("EpochRise")
    var epochRise: Int,
    @SerializedName("EpochSet")
    var epochSet: Int,
    @SerializedName("Rise")
    var rise: String,
    @SerializedName("Set")
    var `set`: String
)