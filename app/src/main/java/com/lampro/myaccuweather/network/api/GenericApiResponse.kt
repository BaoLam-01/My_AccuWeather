package com.lampro.myaccuweather.network.api

import retrofit2.Response

abstract class GenericApiResponse {
    suspend fun <T> apiCall(call: suspend () -> Response<T>): ApiResponse<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let { data ->
                    return ApiResponse.Success(data)
                }
            }
            return ApiResponse.Failed(message = response.message())
        } catch (e: Exception) {
            return ApiResponse.Failed(message = e.message!!)
        }

    }
}