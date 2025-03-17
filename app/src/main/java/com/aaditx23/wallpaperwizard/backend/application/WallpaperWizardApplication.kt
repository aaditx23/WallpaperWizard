package com.aaditx23.wallpaperwizard.backend.application

import android.app.Application
import com.aaditx23.wallpaperwizard.backend.models.QuickSetModel
import com.aaditx23.wallpaperwizard.backend.models.ScheduleModel
import dagger.hilt.android.HiltAndroidApp
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

@HiltAndroidApp
class WallpaperWizardApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        LocalServer.initializeRealm()
        NotificationMan.initializeNotificationChannel(this)
    }

}