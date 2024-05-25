package com.lampro.myaccuweather.objects.uvindexresponse


import com.google.gson.annotations.SerializedName

data class Precip1hr(
    @SerializedName("Imperial")
    var imperial: ImperialXXXXX,
    @SerializedName("Metric")
    var metric: MetricXX
)