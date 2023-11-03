package com.dashxdemo.app.pref

import android.content.Context
import com.dashx.sdk.DashXLog
import com.dashxdemo.app.pref.data.UserData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class AppPref(context: Context) {

    companion object {
        const val PREFERENCE_NAME = "com.dashxdemo.app"

        const val USER_TOKEN = "userToken"

        const val USER_DATA = "userData"

        const val DASH_X_TOKEN = "dashXToken"
    }

    private val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun clearPref() {
        preference.edit().apply {
            remove(USER_TOKEN)
            remove(USER_DATA)
        }.apply()
    }

    fun setUserToken(token: String?) {
        preference.edit().putString(USER_TOKEN, token).apply()
    }

    fun getUserToken(): String? {
        return preference.getString(USER_TOKEN, null)
    }

    fun getUserData(): UserData? {
        val userDataString = preference.getString(USER_DATA, null)
        userDataString?.let {
            return Json { ignoreUnknownKeys = true }.decodeFromString<UserData>(it)
        }

        return null
    }

    fun setUserData(userData: UserData) {
        preference.edit().putString(USER_DATA, Json.encodeToString(userData)).apply()
    }

    fun getDashXToken(): String? {
        return preference.getString(DASH_X_TOKEN, null)
    }

    fun setDashXToken(token: String) {
        preference.edit().putString(DASH_X_TOKEN, token).apply()
    }
}
