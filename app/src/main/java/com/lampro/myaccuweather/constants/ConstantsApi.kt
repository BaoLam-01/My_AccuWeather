package com.lampro.myaccuweather.constants

object ConstantsApi {

    /*
    799lqqQ7AjSkWWO70SyydaRdAzi3O0Bn
    Mf15AZL6AUmk99LwqAgjwd2G4NDwUHY6
    A18DGMLcvebA06oD90XjemEVAp6rR1P6
    3VYmfBZDenK2xi21ERvnfGyCUhqjyV4Y
    O45qbcr1hT0Z7EsD2Gy0RzHDJrHGbhXB
    Dg98nYAeVGcVxD55lEokoKLLypiYTDGC
    Vgx7dBAN35AEjsJ4KgVVSBPHWOIMBaI6
    2qG5lURDwvJGqSRL6Hxl514djaOpJf3c
    lgAOdKCgYiYxqXlCuCxJyBshJrzJyd2o
    * */

    const val API_KEY = "799lqqQ7AjSkWWO70SyydaRdAzi3O0Bn"
    const val BASE_URL = "https://dataservice.accuweather.com/"
    const val GET_CURRENT_WEATHER = "currentconditions/v1/{locationkey}?apikey=$API_KEY&language=vi&details=true"
    const val GET_HOURLY_WEATHER = "forecasts/v1/hourly/12hour/{locationkey}?apikey=$API_KEY&language=vi&details=true&metric=true"
    const val GET_DAILY_WEATHER = "forecasts/v1/daily/5day/{locationkey}?apikey=$API_KEY&language=vi&details=true&metric=true"
    const val GET_DAILY_1DAY_WEATHER = "forecasts/v1/daily/1day/{locationkey}?apikey=$API_KEY&language=vi&details=true&metric=true"
    const val GET_CITY_NAME = "locations/v1/cities/neighbors/{locationkey}?apikey=$API_KEY&language=vi&details=false"
    const val GET_LOCATION_KEY = "locations/v1/cities/geoposition/search?apikey=$API_KEY&q={latitude}%2C{longitude}&language=vi"
    const val GET_INF_BY_CITY_NAME = "locations/v1/cities/search?apikey=$API_KEY&q={cityname}&language=vi&details=true&offset=3"
}