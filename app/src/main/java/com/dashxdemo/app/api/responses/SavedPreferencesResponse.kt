package com.dashxdemo.app.api.responses

import com.google.gson.annotations.SerializedName

data class SavedPreferencesResponse(
    @SerializedName("data") val data: Data,
)

data class Data(
    @SerializedName("saveStoredPreferences") val successResponse: SuccessResponse,
)

data class SuccessResponse(
    @SerializedName("success") val success: Boolean,
)
