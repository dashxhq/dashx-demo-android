package com.dashxdemo.app.api.responses

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("dashx_token")
    val dashXToken: String
)
