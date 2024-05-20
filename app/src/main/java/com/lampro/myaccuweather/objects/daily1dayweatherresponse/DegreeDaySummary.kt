package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class DegreeDaySummary(
    @SerializedName("Cooling")
    var cooling: Cooling,
    @SerializedName("Heating")
    var heating: Heating
)