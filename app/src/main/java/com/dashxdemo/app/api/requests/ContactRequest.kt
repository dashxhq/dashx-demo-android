package com.dashxdemo.app.api.requests

import com.google.gson.annotations.SerializedName

data class ContactRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("feedback")
    val feedback: String
)
