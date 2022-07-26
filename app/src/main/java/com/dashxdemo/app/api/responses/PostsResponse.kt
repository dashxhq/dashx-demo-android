package com.dashxdemo.app.api.responses

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
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("bookmarked_at")
    val bookmarkedAt: String?,
    @SerializedName("user")
    val user: User
)

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("email")
    val email: String
)
