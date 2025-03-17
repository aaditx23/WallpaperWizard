package com.aaditx23.wallpaperwizard.components.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.aaditx23.wallpaperwizard.components.savePref

class WallpaperScheduler(private val context: Context) {



    // AlarmManager for scheduling alarms
    private var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Define Intent for setting wallpaper
    private var setWallpaperIntent = Intent(context, WallpaperAlarmReceiver::class.java).apply {
        putExtra("action", "set_wallpaper")
    }

    // Define Intent for reverting wallpaper
    private var revertWallpaperIntent = Intent(context, WallpaperAlarmReceiver::class.java).apply {
        putExtra("action", "revert_wallpaper")
    }

    // PendingIntent for setting wallpaper
    private var setWallpaperPendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        setWallpaperIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // PendingIntent for reverting wallpaper
    private var revertWallpaperPendingIntent = PendingIntent.getBroadcast(
        context,
        1,
        revertWallpaperIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    fun addId(id: String){
        setWallpaperIntent = Intent(context, WallpaperAlarmReceiver::class.java).apply {
            putExtra("action", "set_wallpaper")
            putExtra("schedule_id", id)
        }
        revertWallpaperIntent = Intent(context, WallpaperAlarmReceiver::class.java).apply {
            putExtra("action", "revert_wallpaper")
            putExtra("schedule_id", id)
        }
        setWallpaperPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            setWallpaperIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        revertWallpaperPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            revertWallpaperIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    // Function to schedule the wallpaper change
    fun scheduleAlarm(startTimeString: String, endTimeString: String) {
        // Define the date format that matches your input time strings
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Parse start and end times from strings to Calendar objects
        val startTime = Calendar.getInstance().apply {
            time = dateFormat.parse(startTimeString) ?: return
        }


        val endTime = Calendar.getInstance().apply {
            time = dateFormat.parse(endTimeString) ?: return
        }

        // Adjust both to today's date
        val today = Calendar.getInstance()
        startTime.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        endTime.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))

        // Schedule alarms (assuming canScheduleExactAlarms and PendingIntents are set up)
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                startTime.timeInMillis,
                setWallpaperPendingIntent
            )

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                endTime.timeInMillis,
                revertWallpaperPendingIntent
            )
            savePref(
                context,
                key = "schedule_status",
                value = "scheduled"
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
