package com.lampro.weatherapp.constants

object ConstantsApi {
    const val API_KEY = "A18DGMLcvebA06oD90XjemEVAp6rR1P6"
    const val BASE_URL = "https://dataservice.accuweather.com/"
    const val GET_CURRENT_WEATHER = "currentconditions/v1/{locationkey}?apikey=$API_KEY&language=vi&details=true"
    const val GET_HOURLY_WEATHER = "forecasts/v1/hourly/12hour/{locationkey}?apikey=$API_KEY&language=vi&details=true&metric=true"
    const val GET_DAILY_WEATHER = "forecasts/v1/daily/5day/{locationkey}?apikey=$API_KEY&language=vi&details=true&metric=true"
    const val GET_DAILY_1DAY_WEATHER = "forecasts/v1/daily/1day/{locationkey}?apikey=$API_KEY&language=vi&details=true&metric=true"
}