package com.dashxdemo.app.api.responses

import com.dashxdemo.app.pref.data.User
import com.google.gson.annotations.SerializedName

data class PostsResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("posts")
    val posts: MutableList<Post>
)

data class Post(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("image")
    val image: AssetData?,
    @SerializedName("video")
    val video: AssetData?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("bookmarked_at")
    val bookmarkedAt: String?,
    @SerializedName("user")
    val user: User
)
