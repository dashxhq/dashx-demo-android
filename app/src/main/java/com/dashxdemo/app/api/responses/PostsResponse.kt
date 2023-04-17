package com.dashxdemo.app.api.responses

import com.dashxdemo.app.pref.data.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostsResponse(
    @SerialName("message")
    val message: String? = null,
    @SerialName("posts")
    val posts: MutableList<Post>
)

@Serializable
data class Post(
    @SerialName("id")
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("text")
    val text: String,
    @SerialName("image")
    val image: AssetData? = null,
    @SerialName("video")
    val video: AssetData?,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("bookmarked_at")
    val bookmarkedAt: String? = null,
    @SerialName("user")
    val user: User? = null
)
