package com.lampro.myaccuweather.objects.locationdata

data class LocationItem(
    var locationKey: String,
    var cityName: String,
    var countryName: String,
    var temp: String,
    var icon : String,
    var lat: Double,
    var lon: Double
)
