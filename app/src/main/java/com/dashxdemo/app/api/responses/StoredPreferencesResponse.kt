package com.dashxdemo.app.api.responses

import com.dashx.sdk.data.Preference
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class StoredPreferences(
    @SerialName("new-bookmark")
    val newBookmark: Preference,
    @SerialName("new-post")
    val newPost: Preference
)
