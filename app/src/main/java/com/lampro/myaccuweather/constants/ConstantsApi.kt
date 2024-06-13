package com.lampro.myaccuweather.constants

object ConstantsApi {

    /*
    4eOArh5GLVcTBQdAg7aFi1I5tw7IC6hF
    aUmqfUZHXKzz9GiGoFzZU8sUSVoOU9nx
    AAuFGbkGboPaya9OAHpKKgzTbLKsFwdT
    fFZgDCHwPErjSOAR6x3RssMrA8NLmSrv

    5rPbGhdgYi2z3xcsAyp8NwyGX7KHIdXw
    66NiI0n5GS0jUWsOdcWClEC62B9CaqtL
    UqscG7k7cynUayVlGDEK4hM8cP1jyvz0
    fReK2GqWoLGoUviBGBKyHjmgNNuuwQWv
    * */

    const val ACCU_API_KEY = "4eOArh5GLVcTBQdAg7aFi1I5tw7IC6hF"
    const val ACCU_API_KEY1 = "aUmqfUZHXKzz9GiGoFzZU8sUSVoOU9nx"
    const val ACCU_API_KEY2 = "AAuFGbkGboPaya9OAHpKKgzTbLKsFwdT"
    const val ACCU_API_KEY3 = "fFZgDCHwPErjSOAR6x3RssMrA8NLmSrv"
    const val ACCU_BASE_URL = "https://dataservice.accuweather.com/"
//    const val GET_CURRENT_WEATHER = "currentconditions/v1/{locationkey}?apikey=$ACCU_API_KEY&language=vi&details=true"

    //https://dataservice.accuweather.com/forecasts/v1/hourly/12hour/3558174?apikey=799lqqQ7AjSkWWO70SyydaRdAzi3O0Bn&language=vi&details=true&metric=true%22
    const val GET_HOURLY_WEATHER = "forecasts/v1/hourly/12hour/{locationkey}?apikey=$ACCU_API_KEY&details=true"
//    const val GET_DAILY_WEATHER = "forecasts/v1/daily/5day/{locationkey}?apikey=$ACCU_API_KEY&language=vi&details=true&metric=true"
//    const val GET_DAILY_1DAY_WEATHER = "forecasts/v1/daily/1day/{locationkey}?apikey=$ACCU_API_KEY&details=true&metric=true"
//    const val GET_CITY_NAME = "locations/v1/cities/neighbors/{locationkey}?apikey=$ACCU_API_KEY&details=false"

    //https://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=799lqqQ7AjSkWWO70SyydaRdAzi3O0Bn&details=false&q=35.6895%2C139.6917&language=vi
    const val GET_LOCATION_KEY = "locations/v1/cities/geoposition/search?apikey=$ACCU_API_KEY1&details=false"
    const val GET_INF_BY_CITY_NAME = "locations/v1/cities/search?apikey=$ACCU_API_KEY2&details=true&offset=3"
    const val GET_UV_INDEX = "currentconditions/v1/{locationkey}?apikey=$ACCU_API_KEY3&details=true"




    ///////////////////
    const val OPEN_API_KEY = "69fd62aa169ae9c49a1bc43f21ac0926"
    const val OPEN_BASE_URL = "https://api.openweathermap.org/"

    //    https://api.openweathermap.org/data/2.5/weather?q=hanoi&appid=69fd62aa169ae9c49a1bc43f21ac0926&units=metric&lang=vi
    const val GET_CURRENT_WEATHER = "data/2.5/weather?appid=$OPEN_API_KEY"

//    https://api.openweathermap.org/data/2.5/forecast?q=hanoi&appid=69fd62aa169ae9c49a1bc43f21ac0926&units=metric&lang=vi
    const val GET_DAILY_WEATHER = "data/2.5/forecast?appid=$OPEN_API_KEY"
//    https://api.openweathermap.org/data/2.5/air_pollution?lat=20.988511&lon=105.771125&appid=69fd62aa169ae9c49a1bc43f21ac0926
    const val GET_AIR_QUALITY = "data/2.5/air_pollution?appid=$OPEN_API_KEY"
    const val GET_GEO_BY_CITY_NAME = "geo/1.0/direct?appid=$OPEN_API_KEY"
}