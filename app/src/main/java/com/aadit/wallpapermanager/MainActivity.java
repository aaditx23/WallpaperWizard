package com.aadit.wallpapermanager;


import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {


    public static Button button, button0, button1, button2, button3, button4;
    public static TextView textView4, textView5, textView6;
    private RecyclerView recyclerView;
    private Schedule schedule;
    public ImageView imageView, imageView1;
    private Bitmap current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        imageView = findViewById(R.id.imageView);
        imageView1 = findViewById(R.id.imageView1);
        button = findViewById(R.id.button);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        schedule = new Schedule(MainActivity.this, this);
        current = schedule.getCurrentWallpaper();
        imageView1.setImageBitmap(current);
        button4.setEnabled(false);



        //Select Image
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schedule.choosePhotoFromGallery();
            }
        });


        //Start Schedule
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(true);
                try {
                    schedule.runner=true;
                    schedule.setWallpaper(textView6, button,button4,button2, button3, button1, imageView1);
                    schedule.thread1.start();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "There has been an error setting the wallpaper", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //Set WP
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    setSelected();
                }
                catch (IOException e){
                    throw new RuntimeException();
                }
            }
        });

        //Start Time
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                schedule.buttonName = "start";
                schedule.timePicker(textView4);
                schedule.runner=true;
            }

        });
        //End time
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schedule.buttonName = "end";
                schedule.timePicker(textView5);
                schedule.runner=true;
                button4.setEnabled(true);
            }

        });
        //Stop
        button4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                schedule.runner=false;
                if(schedule.thread1.getState()==Thread.State.RUNNABLE){
                    Toast.makeText(MainActivity.this, "Could not stop process!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Process Stopped.", Toast.LENGTH_SHORT).show();
                }
                schedule.sHour = 0;
                schedule.eHour = 0;
                schedule.sMinute = 0;
                schedule.eMinute = 0;
                textView4.setText("00 : 00");
                textView5.setText("00 : 00");
                button4.setEnabled(false);
            }
        });
    }





    private void setSelected() throws IOException {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        wallpaperManager.setBitmap(schedule.selectedImageBitmap);
        textView6.setText("Done ");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        current = schedule.getCurrentWallpaper();

        if (requestCode == schedule.REQUEST_IMAGE_SELECT && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            // Start the image cropping activity
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(9,16) // Set the aspect ratio using bitmap dimensions
                    .setCropShape(CropImageView.CropShape.RECTANGLE) // Set the cropping shape to rectangle
                    .setRequestedSize(current.getWidth(), current.getHeight())
                    .setFixAspectRatio(true) // Lock the aspect ratio
                    .start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            // Handle the cropped image result
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                // Get the cropped image URI
                Uri croppedImageUri = result.getUri();

                try {
                    // Load the cropped image into a Bitmap
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedImageUri);

                    // Set the selected image to the ImageView
                    imageView.setImageBitmap(bitmap);

                    // Store the selected image bitmap in the variable
                    schedule.selectedImageBitmap = bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // Handle cropping error
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

}








