package com.dashxdemo.app.api.responses

import com.dashx.sdk.data.Preference
import com.google.gson.annotations.SerializedName

data class StoredPreferencesResponse(
    @SerializedName("new-bookmark")
    val newBookmark: Preference,
    @SerializedName("new-post")
    val newPost: Preference
)
