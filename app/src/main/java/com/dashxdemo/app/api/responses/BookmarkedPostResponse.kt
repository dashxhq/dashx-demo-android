package com.dashxdemo.app.api.responses

import com.google.gson.annotations.SerializedName

data class BookmarkedPostResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("bookmarks")
    val bookmarks: MutableList<Bookmarks>
)

data class Bookmarks(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("user")
    val user: BookmarkedUser,
    @SerializedName("bookmark_id")
    val bookmarkId: Int,
    @SerializedName("bookmarked_at")
    val bookmarkedAt: String
)

data class BookmarkedUser(
    @SerializedName("id")
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("email")
    val email: String
)

