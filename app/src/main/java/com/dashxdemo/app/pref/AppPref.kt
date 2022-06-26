package com.dashxdemo.app.pref

import android.content.Context
import com.dashxdemo.app.api.responses.LoginResponse
import com.dashxdemo.app.api.responses.UserData
import com.google.gson.Gson

class AppPref(context: Context) {

    companion object {
        const val PREFERENCE_NAME = "com.dashxdemo.app"

        const val USER_DATA = "userData"
    }


    private val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    private val gson = Gson()

    fun clearPref() {
        preference.edit().apply {
            remove(USER_DATA)
        }.apply()
    }

    fun setUserData(userData: UserData?) {
        val userDataString = gson.toJson(userData)
        preference.edit().putString(USER_DATA, userDataString).apply()
    }

    fun getUserData(): UserData? {
        val userDataString = preference.getString(USER_DATA, "")
        return gson.fromJson(userDataString, UserData::class.java)
    }
}