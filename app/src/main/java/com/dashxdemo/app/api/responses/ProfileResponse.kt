package com.dashxdemo.app.api.responses

import com.dashxdemo.app.pref.data.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ProfileResponse(
    @SerialName("message")
    val message: String,
    @SerialName("user")
    val user: User
)
