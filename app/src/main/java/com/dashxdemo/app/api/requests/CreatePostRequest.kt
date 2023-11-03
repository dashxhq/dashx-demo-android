package com.dashxdemo.app.api.requests

import com.dashxdemo.app.api.responses.AssetData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CreatePostRequest(
    @SerialName("text")
    val text: String,
    @SerialName("image")
    val image: AssetData? = null,
    @SerialName("video")
    val video: AssetData? = null,
)
