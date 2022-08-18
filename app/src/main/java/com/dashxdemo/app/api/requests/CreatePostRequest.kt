package com.dashxdemo.app.api.requests

import com.dashxdemo.app.api.responses.AssetData
import com.google.gson.annotations.SerializedName

class CreatePostRequest(
    @SerializedName("text")
    val text: String,
    @SerializedName("image")
    val image: AssetData? = null,
    @SerializedName("video")
    val video: AssetData? = null,
)
