package com.dashxdemo.app.api.responses

import kotlinx.serialization.json.JsonObject
import com.dashx.sdk.data.Preference
import com.google.gson.annotations.SerializedName

data class StoredPreferencesResponse(
    @SerializedName("preferenceData")
    val preferenceData: JsonObject
)
data class StoredPreferences(
    @SerializedName("new-bookmark")
    val newBookmark: Preference,
    @SerializedName("new-post")
    val newPost: Preference
)
