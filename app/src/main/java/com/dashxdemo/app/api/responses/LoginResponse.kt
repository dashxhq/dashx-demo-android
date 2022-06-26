package com.dashxdemo.app.api.responses

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("token")
    val token: String
)

data class UserData(
    val email: String,
    val token: String?
)