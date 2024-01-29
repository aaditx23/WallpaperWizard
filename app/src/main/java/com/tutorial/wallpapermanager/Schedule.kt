package com.tutorial.wallpapermanager


import android.app.Activity
import android.app.TimePickerDialog
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import java.util.Calendar


class Schedule {


    companion object{
        var prevWallpaperBitmap: Bitmap? = null
        var selectedWallpaperBitmap: Bitmap? = null
        var wallpaperChangeFlag = false
        var startHour = "0"
        var startMin = "0"
        var endHour = "0"
        var endMin = "0"

        var intentPassed: Intent? = null
        var scheduleRunningFlag = false

        lateinit var context: Context
        lateinit var activity: Activity
        lateinit var c: Colors
        lateinit var buttonStart: Button
        var startTime: String? = null
        var endTime: String? = null

        fun revertCountDown(intent: Intent){
            intent.action = CountDownService.Actions.STOP.toString()
            context.stopService(intent)
            Toast.makeText(context, "RevertCountDownTriggered", Toast.LENGTH_SHORT).show()
            intent.putExtra("signal", "end")
            intent.putExtra("endTime", arrayOf(endHour, endMin))
            intent.action = CountDownService.Actions.START.toString()
            intentPassed = intent
            context.startService(intent)
        }

        fun endService(intent: Intent){
            intent.action = CountDownService.Actions.STOP.toString()
            context.stopService(intent)
        }
    }

    constructor(){
        //do nothing
    }

    constructor( cntxt: Context,  a: Activity){
        context = cntxt
        activity = a
        c = Colors(cntxt)
    }

    fun getCurrentWallpaper(){
        wallpaperChangeFlag=true
        val wallpaperManager = WallpaperManager.getInstance(context)
        var temp = wallpaperManager.drawable
        prevWallpaperBitmap = temp!!.toBitmap(temp.intrinsicWidth, temp.intrinsicHeight, null)
//        return wallpaperManager.drawable

    }

    fun setWallpaper(wp : Bitmap){
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.setBitmap(wp, null, false, WallpaperManager.FLAG_SYSTEM)

    }


    fun showTimePicker(flag: String, view: TextView){
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val min = cal.get(Calendar.MINUTE)
        lateinit var time: String
        val timePickerDialog = TimePickerDialog(
            context,
             { _, h, m ->
                 var processedTime = processTime(h,m)
                 time = String.format("%s : %s %s", processedTime[0], processedTime[1], processedTime[2])
                if (flag == "s"){
                    startHour = h.toString()
                    startMin = m.toString()

                    startTime = time
                }
                else if (flag == "e"){
                    endHour = h.toString()
                    endMin = m.toString()
                    endTime = time
                    buttonStart.isEnabled = true
                }

                Toast.makeText(context, time, Toast.LENGTH_SHORT).show()
                view.setText(time)
                 view.setTextColor(c.palette4green)

            },
            hour, min, false
        )
        timePickerDialog.show()
    }
    fun doWithIntent(intent: Intent){
        context.startService(intent)
        intent.action = CountDownService.Actions.STOP.toString()
    }

    fun processTime(hour: Int, mins: Int): Array<String> {
        var timeArray = arrayOf("0", mins.toString(), "AM")
        if (hour>12){
            timeArray[0] = (hour-12).toString()
            timeArray[2] = "PM"
        }
        else if (hour == 0){
            timeArray[0] = "12"
        }
        else{
            timeArray[0] = hour.toString()
        }

        if (mins< 10){
            timeArray[1] = "0$mins"
        }
        else{
            timeArray[1] = "$mins"
        }
        if (timeArray[0].toInt()<10){
            timeArray[0] = String.format("0%s", timeArray[0])
        }

        return timeArray

    }

}