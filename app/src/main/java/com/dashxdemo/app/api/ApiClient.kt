package com.dashxdemo.app.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient private constructor(private val applicationContext: Context) {
    private val service: ApiService

    companion object {
        const val BASE_URL = "https://api.github.com/"

        private var INSTANCE: ApiClient? = null

        fun getInstance(applicationContext: Context): ApiClient {
            var tempInstance = INSTANCE
            if (tempInstance == null) {
                tempInstance = ApiClient(applicationContext)
                INSTANCE = tempInstance
            }
            return tempInstance
        }
    }

    init {

        val httpClient = OkHttpClient()
        httpClient.networkInterceptors().add(Interceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder.header("Content-Type", "application/json")
            //TODO: Add token to header
            chain.proceed(requestBuilder.build())
        })

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

        service = retrofit.create(ApiService::class.java)

    }

}

