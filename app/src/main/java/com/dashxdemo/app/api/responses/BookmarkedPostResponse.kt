package com.dashxdemo.app.api.responses

import com.google.gson.annotations.SerializedName

data class BookmarkedPostResponse(
    @SerializedName("message")
    val message : String,
    @SerializedName("bookmarks")
    val bookmarks : List<Bookmarks>
)

data class Bookmarks(
    @SerializedName("id") val id: Int,
    @SerializedName("text") val text: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("user") val user: BookmarkedUser,
)

data class BookmarkedUser(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
)

