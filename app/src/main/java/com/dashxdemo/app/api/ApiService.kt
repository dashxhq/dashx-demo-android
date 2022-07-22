package com.dashxdemo.app.api

import com.dashxdemo.app.api.requests.*
import com.dashxdemo.app.api.responses.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("forgot-password")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Call<ForgotPasswordResponse>

    @PATCH("update-profile")
    fun updateProfile(@Body updateProfileRequest: UpdateProfileRequest): Call<UpdateProfileResponse>

    @POST("contact")
    fun contact(@Body contactRequest: ContactRequest): Call<ContactResponse>
}
