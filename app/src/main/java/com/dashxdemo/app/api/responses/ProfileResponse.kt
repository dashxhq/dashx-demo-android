package com.dashxdemo.app.api.responses

import com.dashxdemo.app.pref.data.User
import com.google.gson.annotations.SerializedName

class ProfileResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("user")
    val user: User
)
