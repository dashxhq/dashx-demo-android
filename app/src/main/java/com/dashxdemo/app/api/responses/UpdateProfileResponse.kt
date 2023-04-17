package com.dashxdemo.app.api.responses

import com.dashxdemo.app.pref.data.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class UpdateProfileResponse(
    @SerialName("message")
    val message: String,
    @SerialName("user")
    val user: User
)
