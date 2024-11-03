package com.aaditx23.wallpaperwizard.backend.application

import android.app.Application
import com.aaditx23.wallpaperwizard.backend.models.QuickSetModel
import com.aaditx23.wallpaperwizard.backend.models.ScheduleModel
import dagger.hilt.android.HiltAndroidApp
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

@HiltAndroidApp
class LocalServer: Application() {
    // register models and store it in a variable for accessing all over the application
    companion object{
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()

        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(     // register models here
                    QuickSetModel::class,
                    ScheduleModel::class,
                )
            )
        )
        NotificationMan.initializeNotificationChannel(this)
    }

}