package com.dashxdemo.app.utils

import com.dashxdemo.app.api.responses.ErrorResponse
import com.google.gson.Gson

class Utils {
    companion object {
        fun isValidEmail(emailString: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()
        }

        fun getErrorMessageFromJson(json: String?): String {
            val errorObject = Gson().fromJson(json, ErrorResponse::class.java)
            return errorObject.message ?: ""
        }
    }
}