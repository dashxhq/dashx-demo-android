package com.dashxdemo.app.utils

class Utils {
    companion object {
        fun isValidString(emailString: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()
        }
    }
}