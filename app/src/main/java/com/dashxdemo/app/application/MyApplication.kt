package com.dashxdemo.app.application

import android.app.Application
import com.dashx.sdk.DashXClient
import com.dashx.sdk.DashXLog
import com.dashxdemo.app.BuildConfig

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DashXLog.setLogLevel(DashXLog.LogLevel.DEBUG);
        DashXClient.configure(this, BuildConfig.DASHX_PUBLIC_KEY, BuildConfig.DASHX_BASE_URI, BuildConfig.DASHX_TARGET_ENVIRONMENT)
    }
}
