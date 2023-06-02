package com.aadit.wallpapermanager;

import android.app.Activity;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.aadit.wallpapermanager.MainActivity;
import android.widget.Toast;

import java.io.IOException;

public class SetWallpaper extends Service {
    private boolean isRunning = false;
    private Thread wallpaperThread;
    private WallpaperManager wallpaperManager;
    private Bitmap selectedImageBitmap;
    private int sHour, sMinute, eHour, eMinute;
    public boolean runner;
    public Thread thread1;
    private Schedule s;
    private Context context;
    private Activity activity;
    private TextView status;
    private Button btn;
    private ImageView previous;


    @Override
    public void onCreate() {
        super.onCreate();
        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            isRunning = true;
            selectedImageBitmap = intent.getParcelableExtra("selectedImageBitmap");
            sHour = intent.getIntExtra("sHour", 0);
            sMinute = intent.getIntExtra("sMinute", 0);
            eHour = intent.getIntExtra("eHour", 0);
            eMinute = intent.getIntExtra("eMinute", 0);

            wallpaperThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        stopSelf(); // Stop the service when wallpaper setting is complete
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            wallpaperThread.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setWallpaper(){

    }
}

