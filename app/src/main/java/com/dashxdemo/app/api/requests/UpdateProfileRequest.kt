package com.dashxdemo.app.api.requests

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("email")
    val email: String
)
