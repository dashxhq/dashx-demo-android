package com.dashxdemo.app.pref

import android.content.Context
import com.dashxdemo.app.pref.data.UserData
import com.google.gson.Gson

class AppPref(context: Context) {

    companion object {
        const val PREFERENCE_NAME = "com.dashxdemo.app"

        const val USER_TOKEN = "userToken"

        const val USER_DATA = "userData"
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
        return preference.getString(USER_TOKEN, "")
    }

    fun setUserData(userData: UserData) {
        preference.edit().putString(USER_DATA, Gson().toJson(userData)).apply()
    }

    fun getUserData(): UserData {
        val userDataString = preference.getString(USER_DATA, "")
        return Gson().fromJson(userDataString, UserData::class.java)
    }
}
