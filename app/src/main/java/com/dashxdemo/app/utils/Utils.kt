package com.dashxdemo.app.utils

import android.app.ProgressDialog
import android.content.Context
import com.dashxdemo.app.R
import com.dashxdemo.app.api.responses.ErrorResponse
import com.dashxdemo.app.pref.data.User
import com.dashxdemo.app.pref.data.UserData
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*

class Utils {
    companion object {
        const val USER = "user"
        const val DASHX_TOKEN = "dashx_token"
        const val TOKEN_DELIMITER = "."

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
            return password.isNotEmpty()
        }

        fun initProgressDialog(progressDialog: ProgressDialog, context: Context): ProgressDialog {
            progressDialog.setMessage(context.getString(R.string.loading))
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.setCancelable(false)
            return progressDialog
        }

        private fun decodeToken(token: String?): String {
            val parts = token?.split(TOKEN_DELIMITER)
            return try {
                val charset = charset("UTF-8")
                val payload =
                    String(Base64.getUrlDecoder().decode(parts?.get(1)?.toByteArray(charset)), charset)
                payload
            } catch (e: Exception) {
                "Error parsing JWT: $e"
            }
        }

        fun getUserDataFromToken(token: String?): UserData {
            val decodedToken = decodeToken(token)
            val user = JSONObject(decodedToken).getJSONObject(USER).toString()
            val dashXToken = JSONObject(decodedToken).getString(DASHX_TOKEN)
            return UserData(Gson().fromJson(user, User::class.java), dashXToken)
        }

    }
}
