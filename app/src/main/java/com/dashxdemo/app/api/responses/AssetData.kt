package com.dashxdemo.app.api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssetData(
    @SerialName("status")
    val status: String? = null,
    @SerialName("url")
    val url: String? = null
)
