package com.lampro.myaccuweather.objects.daily1dayweatherresponse


import com.google.gson.annotations.SerializedName

data class AirAndPollen(
    @SerializedName("Category")
    var category: String,
    @SerializedName("CategoryValue")
    var categoryValue: Int,
    @SerializedName("Name")
    var name: String,
    @SerializedName("Type")
    var type: String,
    @SerializedName("Value")
    var value: Int
)