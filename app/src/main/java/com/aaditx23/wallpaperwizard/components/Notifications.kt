package com.aaditx23.wallpaperwizard.components

import android.content.Context
import androidx.core.app.NotificationCompat
import com.aaditx23.wallpaperwizard.R
import com.aaditx23.wallpaperwizard.backend.application.NotificationMan



fun createNotification(
    context: Context,
    title: String,
    bodyText: String
){
    val notificationManager = NotificationMan.notificationManager
    val notification = NotificationCompat
        .Builder(context, "wallpaper_wizard_notification")
        .setContentTitle(title)
        .setContentText(bodyText)
        .setSmallIcon(R.drawable.empty_wallpaper)
        .setStyle(NotificationCompat.BigTextStyle().bigText(bodyText))
        .build()
    notificationManager.notify(6969, notification)

}