package com.aaditx23.wallpaperwizard.backend.application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object NotificationMan {

    lateinit var notificationManager: NotificationManager
        private set

    fun initializeNotificationChannel(context: Context) {
        val notificationChannel = NotificationChannel(
            "wallpaper_wizard_notification",
            "wallpaper_wizard",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Wallpaper Wizard"
            enableVibration(true)
            enableLights(true)
        }

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}
