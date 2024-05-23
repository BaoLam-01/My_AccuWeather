package com.lampro.myaccuweather.objects.infomationcityreponse


import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("EnglishName")
    var englishName: String,
    @SerializedName("ID")
    var iD: String,
    @SerializedName("LocalizedName")
    var localizedName: String
)