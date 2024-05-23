package com.lampro.myaccuweather.objects.infomationcityreponse


import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("DataType")
    var dataType: String,
    @SerializedName("Source")
    var source: String,
    @SerializedName("SourceId")
    var sourceId: Int
)