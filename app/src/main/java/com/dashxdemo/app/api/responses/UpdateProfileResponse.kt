package com.dashxdemo.app.api.responses

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("message")
    val message: String,
)
