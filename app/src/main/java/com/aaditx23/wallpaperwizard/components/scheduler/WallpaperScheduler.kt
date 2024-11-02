package com.aaditx23.wallpaperwizard.components.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class WallpaperScheduler(private val context: Context) {

    // AlarmManager for scheduling alarms
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Define Intent for setting wallpaper
    private val setWallpaperIntent = Intent(context, WallpaperAlarmReceiver::class.java).apply {
        putExtra("action", "set_wallpaper")
    }

    // Define Intent for reverting wallpaper
    private val revertWallpaperIntent = Intent(context, WallpaperAlarmReceiver::class.java).apply {
        putExtra("action", "revert_wallpaper")
    }

    // PendingIntent for setting wallpaper
    private val setWallpaperPendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        setWallpaperIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // PendingIntent for reverting wallpaper
    private val revertWallpaperPendingIntent = PendingIntent.getBroadcast(
        context,
        1,
        revertWallpaperIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Function to schedule the wallpaper change
    fun scheduleAlarm(startTime: Calendar, endTime: Calendar) {
        // Schedule the alarm to set the wallpaper at startTime
        if(alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                startTime.timeInMillis,
                setWallpaperPendingIntent
            )

            // Schedule the alarm to revert the wallpaper at endTime
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                endTime.timeInMillis,
                revertWallpaperPendingIntent
            )
        }
    }

    // Function to cancel the wallpaper schedule
    fun cancelAlarm() {
        // Cancel both the set and revert alarms
        if(alarmManager.canScheduleExactAlarms()) {
            alarmManager.cancel(setWallpaperPendingIntent)
            alarmManager.cancel(revertWallpaperPendingIntent)
        }
    }
}
