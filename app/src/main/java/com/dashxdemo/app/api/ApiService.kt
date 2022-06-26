package com.dashxdemo.app.api

import com.dashxdemo.app.api.requests.LoginRequest
import com.dashxdemo.app.api.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}