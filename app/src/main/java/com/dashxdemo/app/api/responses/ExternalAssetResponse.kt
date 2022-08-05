package com.dashxdemo.app.api.responses

import com.google.gson.annotations.SerializedName
import java.net.URI

data class ExternalAssetResponse(
    @SerializedName("data")
    val externalAsset: ExternalAsset,
)

data class ExternalAsset(
    @SerializedName("id")
    val id: String,
    @SerializedName("installationId")
    val installationId: String,
    @SerializedName("externalColumnId")
    val externalColumnId: String,
    @SerializedName("storageProviderId")
    val storageProviderId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Data,
)

data class Upload(
    @SerializedName("status")
    val status: String,
    @SerializedName("url")
    val url: String
)

data class Data(
    @SerializedName("asset")
    val asset: Asset,
    @SerializedName("upload")
    val upload: Upload
)

data class Asset(
    @SerializedName("metadata")
    val metaData: MetaData,
    @SerializedName("status")
    val status: String,
    @SerializedName("url")
    val url: String
)

data class MetaData(
    @SerializedName("origin-id")
    val originId: String,
)
