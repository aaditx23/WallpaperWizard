package com.tutorial.wallpapermanager

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import java.util.Calendar

class CountDownService: Service() {
    var startArray: Array<String>? = null
    var endArray: Array<String>? =  null
    var signal: String? = null
    var sHour = 0
    var sMin = 0
    var eHour = 0
    var eMin = 0
    val handler = Handler(Looper.getMainLooper())
    val schedule = Schedule()
    lateinit var intentPassed: Intent
    lateinit var runnable: Runnable


    lateinit var notificationManager: NotificationManager
    lateinit var notification: Notification
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        //startForeground(1, notification)
    }



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        if (intent != null) {
            intentPassed = intent
            signal = intent.getStringExtra("signal")
            if (signal =="start"){
                startArray = intent.getStringArrayExtra("startTime")
                sHour = startArray?.get(0)?.toInt() ?: 0
                sMin = startArray?.get(1)?.toInt() ?: 0
                if(sHour !=0 && sMin != 0){
                    val cal = Calendar.getInstance()
                    val cHour = cal.get(Calendar.HOUR_OF_DAY)
                    val cMin = cal.get(Calendar.MINUTE)
                    val cSec = cal.get(Calendar.SECOND)
                    countDown(cHour, cMin, cSec, sHour, sMin, signal!!)
                }
            }
            else if(signal=="end"){
                val cal = Calendar.getInstance()
                val cHour = cal.get(Calendar.HOUR_OF_DAY)
                val cMin = cal.get(Calendar.MINUTE)
                val cSec = cal.get(Calendar.SECOND)
                endArray = intent.getStringArrayExtra("endTime")
                eHour = endArray?.get(0)?.toInt()?: 0
                eMin = endArray?.get(1)?.toInt()?: 0
                if(eHour != 0 && eMin != 0){
                    countDown(cHour, cMin, cSec, eHour, eMin, signal!!)
                }
            }


        }
        when(intent?.action){
            Actions.START.toString() -> startCountDown()//startNotification("Wallpaper Schedule", "Schedule Started")
            Actions.STOP.toString() -> stopService()
        }
        return super.onStartCommand(intent, flags, startId)
    }



    private fun countDown(sH: Int, sM: Int, sS: Int, eH: Int, eM: Int, job: String){

        val startTimeSec = sH * 60 * 60 + sM * 60 + sS
        val endTimeSec = eH * 60 * 60 + eM * 60
        var difference = endTimeSec - startTimeSec
        var flag = true
        runnable = object: Runnable{
            override fun run(){
                if(difference>0 && flag){
                    var setString = ""
                    difference = difference-1
                    if(job == "start"){
                        setString = "Setting Wallpaper in: "
                    }
                    else if (job == "end"){
                        setString = "Reverting Wallpaper in: "
                    }
                    updateNotification("$setString ${secondToTime(difference)}")
                    handler.postDelayed(this,1000)
                }
                else{
                    flag = false
                    handler.removeCallbacks(this)

                    if(job =="start"){
                        schedule.setWallpaper(Schedule.selectedWallpaperBitmap!!)
                        notificationManager.cancel(1)
                        startNotification("Wallpaper Schedule", "Wallpaper Set", NotificationCompat.PRIORITY_HIGH)
                        Schedule.revertCountDown(intentPassed)
                    }
                    else if(job == "end"){
                        schedule.setWallpaper(Schedule.prevWallpaperBitmap!!)
                        notificationManager.cancel(1)
                        startNotificationSilent("Wallpaper Schedule", "Wallpaper Reverted", NotificationCompat.PRIORITY_HIGH)
                        Schedule.endService(intentPassed)
                    }

                }
            }
        }
    }
    private fun startCountDown(){
        handler.post(runnable)
    }
    private fun stopCountDown(){
        handler.removeCallbacks(runnable)
    }
    private fun secondToTime(seconds : Int): String{
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }

    private fun startNotification(title: String, text: String, priority: Int = NotificationCompat.PRIORITY_DEFAULT){
        notification = NotificationCompat.Builder(this, "running_channel")
            //running_channel will need another seperate class which will extend Application
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("$title")
            .setContentText("$text")
            .setPriority(priority)
            .build()
        startForeground(1, notification)
    }

    private fun startNotificationSilent(title: String, text: String, priority: Int = NotificationCompat.PRIORITY_DEFAULT){
        notification = NotificationCompat.Builder(this, "running_channel")
            //running_channel will need another seperate class which will extend Application
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("$title")
            .setContentText("$text")
            .setSound(null)
            .setPriority(priority)
            .build()
        startForeground(1, notification)
    }

    private fun updateNotification(text:String, priority: Int = NotificationCompat.PRIORITY_HIGH){
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val update = NotificationCompat.Builder(this, "running_channel")
            //running_channel will need another seperate class which will extend Application
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Countdown Started")
            .setContentText("$text")
            .setPriority(priority)
            .build()

        update.flags = Notification.FLAG_ONLY_ALERT_ONCE
        notificationManager.notify(1, update)

    }

    private fun stopService(){
        notificationManager.cancel(1)
        stopCountDown()
        stopSelf()
    }

    enum class Actions {
        START, STOP
    }

}