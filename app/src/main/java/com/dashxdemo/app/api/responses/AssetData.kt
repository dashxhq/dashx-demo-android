package com.dashxdemo.app.api.responses

import com.google.gson.annotations.SerializedName

data class AssetData(
    @SerializedName("status")
    val status: String?,
    @SerializedName("url")
    val url: String?
)
