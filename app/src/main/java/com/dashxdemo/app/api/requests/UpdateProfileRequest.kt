package com.dashxdemo.app.api.requests

import com.dashxdemo.app.api.responses.AssetData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("email")
    val email: String,
    @SerialName("avatar")
    val avatar: AssetData? = null,
)
