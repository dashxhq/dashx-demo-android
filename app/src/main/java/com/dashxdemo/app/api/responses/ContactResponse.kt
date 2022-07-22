package com.dashxdemo.app.api.responses

import com.google.gson.annotations.SerializedName

class ContactResponse(
    @SerializedName("message")
    val message: String
)
