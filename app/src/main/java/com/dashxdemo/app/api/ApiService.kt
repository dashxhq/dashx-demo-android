package com.dashxdemo.app.api

import com.dashxdemo.app.api.requests.ForgotPasswordRequest
import com.dashxdemo.app.api.requests.LoginRequest
import com.dashxdemo.app.api.requests.RegisterRequest
import com.dashxdemo.app.api.responses.ForgotPasswordResponse
import com.dashxdemo.app.api.responses.LoginResponse
import com.dashxdemo.app.api.responses.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("forgot-password")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Call<ForgotPasswordResponse>
}