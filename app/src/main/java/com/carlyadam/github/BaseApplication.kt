package com.carlyadam.github

import android.app.Application
import com.bugsnag.android.Bugsnag
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Bugsnag.start(this)
    }
}