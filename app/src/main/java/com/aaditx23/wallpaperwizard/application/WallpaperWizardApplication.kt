package com.aaditx23.wallpaperwizard.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WallpaperWizardApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        LocalServer.initializeRealm()
        NotificationMan.initializeNotificationChannel(this)
    }

}