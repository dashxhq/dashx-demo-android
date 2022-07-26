package com.dashxdemo.app.application

import android.app.Application
import com.dashx.sdk.DashXClient

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DashXClient.createInstance(this,"TLy2w3kxf8ePXEyEjTepcPiq","https://api.dashx-staging.com/graphql","staging")
    }
}
