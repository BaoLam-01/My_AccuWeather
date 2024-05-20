package com.lampro.myaccuweather.objects.currentweatherresponse


import com.google.gson.annotations.SerializedName

data class Speed(
    @SerializedName("Imperial")
    var imperial: ImperialXXXXX,
    @SerializedName("Metric")
    var metric: MetricXX
)