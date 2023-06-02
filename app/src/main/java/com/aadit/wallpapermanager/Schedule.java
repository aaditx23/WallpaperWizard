package com.aadit.wallpapermanager;
import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.Calendar;

public class Schedule {
    public Thread thread1;
    static final int REQUEST_IMAGE_SELECT = 1;
    private static final int REQUEST_CODE_PERMISSION = 456;
    private Activity activity;
    private Context context;
    private String sTime;
    public int sHour=0, sMinute=0, eHour=0, eMinute=0;
    public String buttonName = "None";
    public Bitmap selectedImageBitmap=null;
    public boolean runner = true;
    Calendar calendar = Calendar.getInstance();
    public Schedule(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;


    }

    public void choosePhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }
    public void timePicker(TextView textView){
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // Create a time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the selected time
                        // hourOfDay and minute variables will contain the selected time
                        // Perform any required actions with the selected time
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);
                        if(buttonName.equals("start")){
                            sHour = hourOfDay; sMinute = minute;
                        }
                        else if(buttonName.equals("end")){
                            eHour = hourOfDay; eMinute = minute;
                        }
                        sTime = hourOfDay +" : "+ minute;
                        textView.setText(sTime);
                    }
                }, currentHour, currentMinute, false);

        // Show the time picker dialog

        timePickerDialog.show();

    }
    public Bitmap getCurrentWallpaper() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request the READ_EXTERNAL_STORAGE permission if it's not granted
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            return null;
        }

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();

        if (wallpaperDrawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) wallpaperDrawable).getBitmap();
        }

        return null;
    }
    public int[] getTimeFromTextView(TextView textView){
        String[] temp = textView.getText().toString().split(" : ",0);

        int[] hour_minute = {Integer.parseInt(temp[0]), Integer.parseInt(temp[1])};
        System.out.println(temp[0]+" "+temp[1]);
        return hour_minute;
    }
    public int getTimeDifference(int hourS, int hourE, int minS, int minE) {
        int min;
        int minStart = hourS*60 + minS;
        int minEnd =  hourE*60 + minE;
        if(minStart>minEnd){
            min = (1440-minStart)+minEnd;
        }
        else{
            min = minEnd-minStart;
        }
        return min*60;
    }
    public int getTimeDifferenceFromCurrent(int hourS, int minS) {
        calendar.setTimeInMillis(System.currentTimeMillis());
        int cHour = calendar.get(Calendar.HOUR_OF_DAY);
        int cMinute = calendar.get(Calendar.MINUTE);
        int cSec = calendar.get(Calendar.SECOND);
        if (hourS==cHour && minS==cMinute) {
            return 0;
        }
        int getSec = getTimeDifference(cHour, hourS, cMinute, minS);
        if (getSec==60) {
            return 60-cSec;
        }
        return getSec-cSec;
    }

    public void setWallpaper(TextView status,
                             Button btn, ImageView previous) throws IOException, InterruptedException {

        Bitmap current = getCurrentWallpaper();
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());
        final int count = getTimeDifferenceFromCurrent(sHour, sMinute);
        if (count==0){
            Toast.makeText(activity, "Please select proper time interval." , Toast.LENGTH_SHORT).show();
        }
        final int duration = getTimeDifference(sHour, eHour, sMinute, eMinute);
        if (duration==0){
            Toast.makeText(activity, "Please select longer interval", Toast.LENGTH_SHORT).show();
        }
        status.setText(("Countdown: "+count));
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(runner){
                    int countDown = count;
                    int durationCounter = duration;
                    try {
                        while (countDown > 0 && runner) {
                            final int finalCountDown = countDown;
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {status.setText("Countdown: " + finalCountDown);
                                }
                            });
                            Thread.sleep(1000);
                            countDown--;
                        }

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                status.setText("Countdown over!");
                            }
                        });
                        if(selectedImageBitmap==null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Please Select an image first", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else if(sHour == 0 && sMinute ==0 && eHour==0 && eMinute==0){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Please select a time interval", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            wallpaperManager.setBitmap(selectedImageBitmap);
                        }

                        while (durationCounter > 0 && runner) {
                            final int finalCountDown = durationCounter;
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {status.setText("Stop After: " + finalCountDown);
                                }
                            });
                            Thread.sleep(1000);
                            durationCounter--;
                        }
                        wallpaperManager.setBitmap(current);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btn.setEnabled(true);
                                if(!runner) status.setText("Aborted");
                                else status.setText("Task Complete");
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



            }

        }, "Thread1");





    }




}
