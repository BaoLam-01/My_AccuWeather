package com.lampro.myaccuweather.objects.currentweatherresponse


import com.google.gson.annotations.SerializedName

data class PressureTendency(
    @SerializedName("Code")
    var code: String,
    @SerializedName("LocalizedText")
    var localizedText: String
)