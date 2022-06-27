package com.dashxdemo.app.pref

import android.content.Context

class AppPref(context: Context) {

    companion object {
        const val PREFERENCE_NAME = "com.dashxdemo.app"

        const val USER_TOKEN = "userToken"
    }

    private val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun clearPref() {
        preference.edit().apply {
            remove(USER_TOKEN)
        }.apply()
    }

    fun setUserToken(token: String?) {
        preference.edit().putString(USER_TOKEN, token).apply()
    }

    fun getUserToken(): String? {
        return preference.getString(USER_TOKEN, "")
    }
}
