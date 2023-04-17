package com.dashxdemo.app.api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("message")
    val message: String,
    @SerialName("token")
    val token: String,
    @SerialName("dashx_token")
    val dashXToken: String
)
