package com.lampro.myaccuweather.objects.locationkeyresponse


import com.google.gson.annotations.SerializedName

data class TimeZone(
    @SerializedName("Code")
    var code: String,
    @SerializedName("GmtOffset")
    var gmtOffset: Int,
    @SerializedName("IsDaylightSaving")
    var isDaylightSaving: Boolean,
    @SerializedName("Name")
    var name: String,
    @SerializedName("NextOffsetChange")
    var nextOffsetChange: Any
)