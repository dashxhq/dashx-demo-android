package com.dashxdemo.app.api

import android.content.Context
import com.dashxdemo.app.api.requests.ForgotPasswordRequest
import com.dashxdemo.app.api.requests.LoginRequest
import com.dashxdemo.app.api.requests.RegisterRequest
import com.dashxdemo.app.api.responses.ForgotPasswordResponse
import com.dashxdemo.app.api.responses.LoginResponse
import com.dashxdemo.app.api.responses.RegisterResponse
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient private constructor(private val applicationContext: Context) {
    private val service: ApiService

    companion object {
        const val BASE_URL = "https://node.dashxdemo.com/"

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

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ApiService::class.java)

    }

    fun login(loginRequest: LoginRequest, callback: Callback<LoginResponse>) {
        val call = service.login(loginRequest)
        call.enqueue(callback)
    }

    fun register(registerRequest: RegisterRequest, callback: Callback<RegisterResponse>) {
        val call = service.register(registerRequest)
        call.enqueue(callback)
    }

    fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest, callback: Callback<ForgotPasswordResponse>) {
        val call = service.forgotPassword(forgotPasswordRequest)
        call.enqueue(callback)
    }

}

