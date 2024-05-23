package com.lampro.myaccuweather.network.api

import com.lampro.myaccuweather.constants.ConstantsApi
import com.lampro.myaccuweather.network.api.request.IWeatherService
import jp.co.cyberagent.android.gpuimage.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private val instance by lazy {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
            val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            Retrofit.Builder().baseUrl(ConstantsApi.BASE_URL).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
        val getWeatherApi:IWeatherService by lazy {
            instance.create(IWeatherService::class.java)
        }
    }
}