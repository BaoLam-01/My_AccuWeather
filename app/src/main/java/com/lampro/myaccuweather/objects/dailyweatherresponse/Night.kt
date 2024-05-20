package com.lampro.myaccuweather.objects.dailyweatherresponse


import com.google.gson.annotations.SerializedName

data class Night(
    @SerializedName("HasPrecipitation")
    var hasPrecipitation: Boolean,
    @SerializedName("Icon")
    var icon: Int,
    @SerializedName("IconPhrase")
    var iconPhrase: String,
    @SerializedName("PrecipitationIntensity")
    var precipitationIntensity: String,
    @SerializedName("PrecipitationType")
    var precipitationType: String
)