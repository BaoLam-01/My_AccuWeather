package com.lampro.myaccuweather.network.api.clients

import com.lampro.myaccuweather.constants.ConstantsApi
import com.lampro.myaccuweather.network.api.request.IAccuWeatherService
import jp.co.cyberagent.android.gpuimage.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AccuClient {
    companion object {
        private val instance by lazy {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
            val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            Retrofit.Builder().baseUrl(ConstantsApi.ACCU_BASE_URL).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
        val getAccuWeatherApi:IAccuWeatherService by lazy {
            instance.create(IAccuWeatherService::class.java)
        }
    }
}