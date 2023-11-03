package com.dashxdemo.app.pref.data

import com.dashxdemo.app.api.responses.AssetData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    @SerialName("user")
    val userData: User? = null
)

@Serializable
data class User(
    @SerialName("id")
    val id: Int,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("email")
    val email: String,
    @SerialName("avatar")
    val avatar: AssetData? = null
)
