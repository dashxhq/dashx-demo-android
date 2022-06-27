package com.dashxdemo.app.utils

import android.content.Context
import com.dashxdemo.app.R
import com.dashxdemo.app.api.responses.ErrorResponse
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson

class Utils {
    companion object {
        private fun isValidEmail(emailString: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()
        }

        fun getErrorMessageFromJson(json: String?): String {
            val errorObject = Gson().fromJson(json, ErrorResponse::class.java)
            return errorObject.message ?: ""
        }

        fun validateEmail(emailId: String, textInput: TextInputLayout, context: Context): Boolean {
            if (emailId.isEmpty()) {
                textInput.isErrorEnabled = true
                textInput.error = context.getString(R.string.email_required_text)

            } else if (!isValidEmail(emailId)) {
                textInput.isErrorEnabled = true
                textInput.error = context.getString(R.string.valid_email_text)
            } else {
                textInput.isErrorEnabled = false
                textInput.error = null
                return true
            }
            return false
        }

        fun validatePassword(
            password: String,
            textInput: TextInputLayout,
            context: Context
        ): Boolean {
            if (password.isEmpty()) {
                textInput.isErrorEnabled = true
                textInput.error = context.getString(R.string.password_required_text)
            }
            return !password.isEmpty()
        }
    }
}
