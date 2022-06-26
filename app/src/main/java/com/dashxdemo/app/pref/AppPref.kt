package com.dashxdemo.app.pref

import android.content.Context

class AppPref(context: Context) {

    val PREFERENCE_NAME = "com.dashxdemo.app"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun clearPref(){
        //TODO: Clear all preferences
    }
}