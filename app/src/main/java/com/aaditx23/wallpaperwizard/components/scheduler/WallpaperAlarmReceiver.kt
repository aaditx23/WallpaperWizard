package com.aaditx23.wallpaperwizard.components.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.aaditx23.wallpaperwizard.components.JpgToBitmapAsync
import com.aaditx23.wallpaperwizard.components.createNotification
import com.aaditx23.wallpaperwizard.components.savePref
import com.aaditx23.wallpaperwizard.components.setWallpaper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WallpaperAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.getStringExtra("action")) {
            "set_wallpaper" -> {
                Toast.makeText(context, "Setting wallpaper...", Toast.LENGTH_SHORT).show()
                val id = intent.getStringExtra("schedule_id")
                id?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        savePref(
                            context,
                            key = "schedule_id",
                            value = id
                        )
                        savePref(
                            context,
                            key = "schedule_status",
                            value = "started"
                        )
                        val home = JpgToBitmapAsync(
                            context,
                            fullPath = "schedule/$id/selectedHome.jpg"
                        )
                        val lock = JpgToBitmapAsync(
                            context,
                            fullPath = "schedule/$id/selectedLock.jpg"
                        )
                        home?.let {
                            val result = setWallpaper(context, home, 0)
                            if(!result){
                                Toast.makeText(context, "Could not set Home Screen", Toast.LENGTH_SHORT).show()
                            }
                        }
                        lock?.let {
                            val result = setWallpaper(context, lock, 1)
                            if(!result){
                                Toast.makeText(context, "Could not set Lock Screen", Toast.LENGTH_SHORT).show()
                            }
                        }
                        createNotification(
                            context,
                            title = "Schedule Started",
                            bodyText = "Wallpapers Set"
                        )

                    }
                }
            }
            "revert_wallpaper" -> {
                // Call your function to revert to the previous wallpaper
                Toast.makeText(context, "Reverting wallpaper...", Toast.LENGTH_SHORT).show()
                val id = intent.getStringExtra("schedule_id")
                id?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        savePref(
                            context,
                            key = "schedule_status",
                            value = "completed"
                        )
                        val home = JpgToBitmapAsync(
                            context,
                            fullPath = "schedule/$id/prevHome.jpg"
                        )
                        val lock = JpgToBitmapAsync(
                            context,
                            fullPath = "schedule/$id/prevLock.jpg"
                        )
                        home?.let {
                            val result = setWallpaper(context, home, 0)
                            if(!result){
                                Toast.makeText(context, "Could not set Home Screen", Toast.LENGTH_SHORT).show()
                            }
                        }
                        lock?.let {
                            val result = setWallpaper(context, lock, 1)
                            if(!result){
                                Toast.makeText(context, "Could not set Lock Screen", Toast.LENGTH_SHORT).show()
                            }
                        }
                        createNotification(
                            context,
                            title = "Schedule Completed",
                            bodyText = "Wallpapers Reverted"
                        )

                    }
                }

            }
            else -> {
                Toast.makeText(context, "Unknown action", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
