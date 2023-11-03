package com.dashxdemo.app.application

import android.app.Application
import com.dashx.sdk.DashX
import com.dashx.sdk.DashXActivityLifecycleCallbacks
import com.dashx.sdk.DashXLog
import com.dashxdemo.app.BuildConfig

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DashXLog.setLogLevel(DashXLog.LogLevel.DEBUG)
        DashX.configure(
            context = this,
            publicKey = BuildConfig.DASHX_PUBLIC_KEY,
            baseURI = BuildConfig.DASHX_BASE_URI,
            targetEnvironment = BuildConfig.DASHX_TARGET_ENVIRONMENT
        )

        DashXActivityLifecycleCallbacks.enableActivityLifecycleTracking(this)
    }
}
