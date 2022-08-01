package com.dashxdemo.app.api.requests

import com.google.gson.annotations.SerializedName

class CreatePostRequest(
    @SerializedName("text")
    val text: String
)
