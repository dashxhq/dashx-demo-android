package com.dashxdemo.app.pref.data

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("user")
    val userData: User,
    @SerializedName("dashx_token")
    val token: String
)

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("email")
    val email: String
)
