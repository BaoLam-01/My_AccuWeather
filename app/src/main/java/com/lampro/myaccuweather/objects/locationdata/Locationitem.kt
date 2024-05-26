package com.lampro.myaccuweather.objects.locationdata

data class Locationitem(
    var locationKey: String,
    var cityName: String,
    var countryName: String,
    var temp: String,
    var lat: Double,
    var lon: Double
)
