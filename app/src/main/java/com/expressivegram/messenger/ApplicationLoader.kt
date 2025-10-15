package com.expressivegram.messenger

import android.app.Application
import android.content.Context
import com.expressivegram.messenger.utils.DownloadController

class ApplicationLoader : Application() {
    companion object {
        lateinit var applicationContext: Context private set
    }

    override fun onCreate() {
        super.onCreate()
        Companion.applicationContext = this
        DownloadController.initialize()
    }
}
