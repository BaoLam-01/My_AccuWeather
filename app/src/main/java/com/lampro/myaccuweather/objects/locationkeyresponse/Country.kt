package com.lampro.myaccuweather.objects.locationkeyresponse


import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("EnglishName")
    var englishName: String,
    @SerializedName("ID")
    var iD: String,
    @SerializedName("LocalizedName")
    var localizedName: String
)