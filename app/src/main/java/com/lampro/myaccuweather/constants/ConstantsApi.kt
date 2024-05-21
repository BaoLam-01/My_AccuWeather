package com.lampro.weatherapp.constants

object ConstantsApi {

    /*
    799lqqQ7AjSkWWO70SyydaRdAzi3O0Bn
    Mf15AZL6AUmk99LwqAgjwd2G4NDwUHY6
    A18DGMLcvebA06oD90XjemEVAp6rR1P6
    3VYmfBZDenK2xi21ERvnfGyCUhqjyV4Y
    O45qbcr1hT0Z7EsD2Gy0RzHDJrHGbhXB
    * */

    const val API_KEY = "3VYmfBZDenK2xi21ERvnfGyCUhqjyV4Y"
    const val BASE_URL = "https://dataservice.accuweather.com/"
    const val GET_CURRENT_WEATHER = "currentconditions/v1/{locationkey}?apikey=$API_KEY&language=vi&details=true"
    const val GET_HOURLY_WEATHER = "forecasts/v1/hourly/12hour/{locationkey}?apikey=$API_KEY&language=vi&details=true&metric=true"
    const val GET_DAILY_WEATHER = "forecasts/v1/daily/5day/{locationkey}?apikey=$API_KEY&language=vi&details=true&metric=true"
    const val GET_DAILY_1DAY_WEATHER = "forecasts/v1/daily/1day/{locationkey}?apikey=$API_KEY&language=vi&details=true&metric=true"
}