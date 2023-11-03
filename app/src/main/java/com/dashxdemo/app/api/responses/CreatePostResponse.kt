package com.dashxdemo.app.api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CreatePostResponse(
    @SerialName("message")
    val message: String,
    @SerialName("post")
    val post: Post
)
