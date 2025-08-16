package com.aaditx23.wallpaperwizard.ui.components.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.aaditx23.wallpaperwizard.models.ScheduleModel
import com.aaditx23.wallpaperwizard.ui.components.JpgToBitmapAsync
import com.aaditx23.wallpaperwizard.ui.components.createNotification
import com.aaditx23.wallpaperwizard.ui.components.getCroppedStoragePath
import com.aaditx23.wallpaperwizard.ui.components.savePref
import com.aaditx23.wallpaperwizard.ui.components.setWallpaper
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class WallpaperAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            val idFromIntent = intent.getStringExtra("schedule_id")
            val realm = Realm.open(
                RealmConfiguration.Builder(schema = setOf(ScheduleModel::class)).build()
            )
            idFromIntent.let { id ->
                val schedule = realm.query<ScheduleModel>("_id = $0", ObjectId(id!!)).first().find()
                when (intent.getStringExtra("action")) {
                    "set_wallpaper" -> {
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "Setting wallpaper...", Toast.LENGTH_SHORT).show()
                        }

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
                            schedule?.let {
                                if (it.scheduledHome.isNotEmpty()) {
                                    if (setWallpaper(context, 0, it.scheduledHome)) {
                                        withContext(Dispatchers.Main){
                                            Toast.makeText(
                                                context,
                                                "Could not set Home Screen",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                                if (it.scheduledLock.isNotEmpty()) {
                                    if (setWallpaper(context, 1, it.scheduledLock)) {
                                        withContext(Dispatchers.Main){
                                            Toast.makeText(
                                                context,
                                                "Could not set Lock Screen",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                                createNotification(
                                    context,
                                    title = "Schedule Started",
                                    bodyText = "Wallpapers Set"
                                )
                            }

                    }

                    "revert_wallpaper" -> {
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "Reverting wallpaper...", Toast.LENGTH_SHORT).show()
                        }
                            savePref(
                                context,
                                key = "schedule_status",
                                value = "idle"
                            )
                            schedule?.let {
                                if (it.prevHome.isNotEmpty()) {
                                    if (setWallpaper(context, 0, it.prevHome)) {
                                        withContext(Dispatchers.Main){
                                            Toast.makeText(
                                                context,
                                                "Could not set Home Screen",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                                if (it.prevLock.isNotEmpty()) {
                                    if (setWallpaper(context, 1, it.prevLock)) {
                                        withContext(Dispatchers.Main){
                                            Toast.makeText(
                                                context,
                                                "Could not set Lock Screen",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                                createNotification(
                                    context,
                                    title = "Schedule Completed",
                                    bodyText = "Wallpapers Reverted"
                                )
                            }

                    }
                    else -> {
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "Unknown action", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }

        }
    }
}
