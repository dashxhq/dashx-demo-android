package com.dashxdemo.app.api.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ContactResponse(
    @SerialName("message")
    val message: String
)
