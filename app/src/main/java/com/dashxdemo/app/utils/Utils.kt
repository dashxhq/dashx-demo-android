package com.dashxdemo.app.utils

import android.app.ProgressDialog
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import com.dashxdemo.app.R
import com.dashxdemo.app.api.responses.ErrorResponse
import com.dashxdemo.app.pref.data.User
import com.dashxdemo.app.pref.data.UserData
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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

        fun validatePassword(password: String, textInput: TextInputLayout, context: Context): Boolean {
            if (password.isEmpty()) {
                textInput.isErrorEnabled = true
                textInput.error = context.getString(R.string.password_required_text)
            }
            return password.isNotEmpty()
        }

        fun validateNameFields(firstNameTextInput: TextInputLayout, lastNameTextInput: TextInputLayout, context: Context): Boolean {
            if (firstNameTextInput.editText?.text.isNullOrEmpty()) {
                firstNameTextInput.isErrorEnabled = true
                firstNameTextInput.error = context.getString(R.string.first_name_required_text)
                return false
            }

            if (lastNameTextInput.editText?.text.isNullOrEmpty()) {
                lastNameTextInput.isErrorEnabled = true
                lastNameTextInput.error = context.getString(R.string.last_name_required_text)
                return false
            }
            return true
        }

        fun validateFeedbackEditFields(nameTextInput: TextInputLayout, feedbackTextInput: TextInputLayout, emailTextInput: TextInputLayout, context: Context): Boolean {
            if (nameTextInput.editText?.text.toString().trim().isNullOrEmpty()) {
                nameTextInput.isErrorEnabled = true
                nameTextInput.error = context.getString(R.string.name_required)
                return false
            }

            validateEmail(emailTextInput.editText?.text.toString(), emailTextInput, context)

            if (feedbackTextInput.editText?.text.toString().trim().isNullOrEmpty()) {
                feedbackTextInput.isErrorEnabled = true
                feedbackTextInput.error = context.getString(R.string.feedback_required)
                return false
            }
            return true
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
                val payload = String(Base64.getUrlDecoder().decode(parts?.get(1)?.toByteArray(charset)), charset)
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

        fun showToast(context: Context, string: String) {
            Toast.makeText(context, string, Toast.LENGTH_LONG).show()
        }

        fun runOnUiThread(runBlock: () -> Unit) {
            Handler(Looper.getMainLooper()).post {
                runBlock.invoke()
            }
        }

        fun timeAgo(dateString: String?): String? {
            var convertedTime: String? = null
            val prefix = "Posted"
            val suffix = "ago"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            val date: Date = dateFormat.parse(dateString)

            val dateDiff: Long = Calendar.getInstance().timeInMillis - date.time
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)

            if (second < 60) {
                convertedTime = "$prefix a few seconds $suffix"
            } else if (second > 60 && minute < 60) {
                convertedTime = "$prefix $minute minutes $suffix"
            } else if (second > 60 && minute > 60 && hour < 24) {
                convertedTime = "$prefix ${hour + 1} hours $suffix"
            } else if (second > 60 && minute > 60 && hour > 24 && hour < 48) {
                convertedTime = "$prefix a day $suffix"
            } else if (day > 360) {
                convertedTime = prefix + (day / 360).toString() + " years " + suffix
            } else if (day < 30) {
                convertedTime = "$prefix ${day + 1} days $suffix"
            } else if (day > 30) {
                convertedTime = prefix + ((day / 30) + 1).toString() + " months " + suffix
            }
            return convertedTime
        }

        fun getPath(context: Context, uri: Uri?): String? {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor = context.contentResolver.query(uri!!, projection, null, null, null) ?: return null
            val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val path: String = cursor.getString(columnIndex)
            cursor.close()
            return path
        }

        fun getFileFromBitmap(bitmap: Bitmap, context: Context): File {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path: String = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "", null)
            val uri = Uri.parse(path)
            return File(getPath(context, uri)!!)
        }

        fun getInitials(name: String): String {
            if (name.isEmpty()) {
                return name
            }
            val nameSplit = name.split(" ")
            return if (nameSplit.size > 1) {
                nameSplit[0][0].toString() + nameSplit[1][0].toString()
            } else {
                nameSplit[0][0].toString()
            }
        }
    }
}
