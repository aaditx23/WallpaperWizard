package com.aaditx23.wallpaperwizard.components.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class WallpaperAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.getStringExtra("action")) {
            "set_wallpaper" -> {
                Toast.makeText(context, "Setting wallpaper...", Toast.LENGTH_SHORT).show()
            }
            "revert_wallpaper" -> {
                // Call your function to revert to the previous wallpaper
                Toast.makeText(context, "Reverting wallpaper...", Toast.LENGTH_SHORT).show()
                // Add your code to revert the wallpaper
            }
            else -> {
                Toast.makeText(context, "Unknown action", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
