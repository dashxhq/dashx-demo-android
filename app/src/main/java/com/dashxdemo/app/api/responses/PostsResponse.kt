package com.dashxdemo.app.api.responses

import com.google.gson.annotations.SerializedName

data class PostsResponse(
    @SerializedName("posts") val posts: List<Post>,
)

data class Post(
    @SerializedName("id") val id: Int,
    @SerializedName("text") val text: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("user") val user: User,
    @SerializedName("bookmarked_at") val bookmarkedAt: String?,
)

data class User(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
)
