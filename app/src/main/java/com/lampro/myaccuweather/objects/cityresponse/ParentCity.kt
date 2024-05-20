package com.lampro.myaccuweather.objects.cityresponse


import com.google.gson.annotations.SerializedName

data class ParentCity(
    @SerializedName("EnglishName")
    var englishName: String,
    @SerializedName("Key")
    var key: String,
    @SerializedName("LocalizedName")
    var localizedName: String
)