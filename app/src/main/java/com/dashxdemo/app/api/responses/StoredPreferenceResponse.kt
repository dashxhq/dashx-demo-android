package com.dashxdemo.app.api.responses

import com.google.gson.annotations.SerializedName

data class StoredPreferenceResponse(
    @SerializedName("new-bookmark")
    val newBookmark: StoredPreferenceData,
    @SerializedName("new-post")
    val newPost: StoredPreferenceData
)

data class StoredPreferenceData(
    @SerializedName("email")
    val email: Boolean,
    @SerializedName("enabled") var enabled: Boolean,
    @SerializedName("push")
    val push: Boolean,
    @SerializedName("sms")
    val sms: Boolean,
    @SerializedName("whatsapp")
    val whatsapp: Boolean
)
