package com.dashxdemo.app.api.responses

import com.google.gson.annotations.SerializedName

class CreatePostResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("post")
    val post: Post
)
