package com.tutorial.wallpapermanager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    lateinit var StartTime: TextView
    lateinit var EndTime: TextView
    lateinit var Status: TextView
    lateinit var selectTextView: TextView
    lateinit var SelectedWP: ImageView
    lateinit var PrevWP: ImageView
    lateinit var buttonStartSchedule: Button
    lateinit var buttonStop: Button

    lateinit var schedule: Schedule
    lateinit var c: Colors
    var selectedImageBitmap: Bitmap? = null



    var selectedImageUri: Uri? = null
    var selectedWallpaperDrawable: Drawable? = null


    val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value
                if (!isGranted){
                    Toast.makeText(this@MainActivity, "Permission Denied $permissionName", Toast.LENGTH_SHORT).show()
                    showRationaleDialog("Permission Denied", "Permissions are required to use the application. Please grant them manually.")
                    return@registerForActivityResult
                }
            }
        }






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestRequiredPermission()
        StartTime = findViewById(R.id.textView4);
        EndTime = findViewById(R.id.textView5);
        Status = findViewById(R.id.textView6);
        selectTextView = findViewById(R.id.textView2)
        SelectedWP = findViewById(R.id.imageView);
        PrevWP = findViewById(R.id.imageView1);
        buttonStartSchedule = findViewById(R.id.button);
        buttonStop = findViewById(R.id.button4);
        //cropImageView = findViewById(R.id.cropImageView)

        schedule = Schedule(this, this)
        c = Colors(this@MainActivity)
        var borderRed = GradientDrawable()
        borderRed.setColor(Color.TRANSPARENT)
        borderRed.setStroke(2, c.palette2DarkRed)



        buttonStartSchedule.isEnabled = false
        Schedule.buttonStart = buttonStartSchedule
        SelectedWP.background = borderRed
        selectTextView.setTextColor(c.palette2DarkRed)

        StartTime.setTextColor(c.palette2DarkRed)
        EndTime.setTextColor(c.palette2DarkRed)

        requestRequiredPermission()
        consistencyCheck()




        //https://github.com/CanHub/Android-Image-Cropper



        val cropImage = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                // Use the returned uri.
                val uriContent = result.uriContent
                selectedImageBitmap = resizeBitmap(uriToBitmap(uriContent, this))
                Schedule.selectedWallpaperBitmap = selectedImageBitmap
                SelectedWP.setImageBitmap(Schedule.selectedWallpaperBitmap)
                selectTextView.setText("Wallpaper Selected")
                selectTextView.setTextColor(c.palette4green)

            } else {
                // An error occurred.
                val exception = result.error
            }
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val uri: Uri? = data.data
                    if (uri != null) {
                        selectedImageUri = uri
                        var cio = CropImageOptions()
                        cio.allowRotation = true
                        cio.fixAspectRatio = true
                        cio.aspectRatioY = 16
                        cio.aspectRatioX = 9
                        cio.activityTitle = "Crop Image"

                        cropImage.launch(
                            CropImageContractOptions(selectedImageUri, cio)
                        )


                    }
                }
            }
        }


        SelectedWP.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImage.launch(galleryIntent)
        }


        StartTime.setOnClickListener{
            schedule.showTimePicker("s", StartTime)
        }
        EndTime.setOnClickListener{
            schedule.showTimePicker("e", EndTime)
        }
        buttonStartSchedule.setOnClickListener {
            if(!errorFound()){
                val intent = Intent(applicationContext, CountDownService::class.java)
                Schedule.intentPassed = intent
                Toast.makeText(this, "${Schedule.startHour} : ${Schedule.startMin} to ${Schedule.endHour} : ${Schedule.endMin}", Toast.LENGTH_SHORT).show()
                intent.putExtra("startTime", arrayOf(Schedule.startHour, Schedule.startMin))
                intent.putExtra("signal", "start")
                intent.action = CountDownService.Actions.START.toString()
                startService(intent)
                schedule.doWithIntent(intent)
            }


        }
        
        buttonStop.setOnClickListener { 
            if(Schedule.intentPassed == null){
                Toast.makeText(this, "The schedule has not started yet or has been completed", Toast.LENGTH_SHORT).show()
            }
            else{
//                Schedule.intentPassed!!.action = CountDownService.Actions.STOP.toString()
//                stopService(Schedule.intentPassed)
//                Toast.makeText(this, "Scheduled job has been cancelled.", Toast.LENGTH_SHORT).show()
                Schedule.endService(Schedule.intentPassed!!)
            }
        }

    }

    private fun consistencyCheck(){
        if(Schedule.wallpaperChangeFlag==false){
            schedule.getCurrentWallpaper()
            Schedule.wallpaperChangeFlag=true

        }
        if(Schedule.selectedWallpaperBitmap != null){
            SelectedWP.setImageBitmap(Schedule.selectedWallpaperBitmap)
        }
        if(Schedule.startTime != null){
            StartTime.text = Schedule.startTime
            StartTime.setTextColor(c.palette4green)
        }
        if(Schedule.endTime != null){
            EndTime.text = Schedule.endTime
            EndTime.setTextColor(c.palette4green)
            buttonStartSchedule.isEnabled = true
        }
        PrevWP.setImageBitmap(Schedule.prevWallpaperBitmap)
    }

    private fun errorFound(): Boolean{
        if(Schedule.selectedWallpaperBitmap == null){
            Toast.makeText(this, "Select a wallpaper to set", Toast.LENGTH_SHORT).show()
            return true
        }
        if(Schedule.startHour.toInt() == 0 && Schedule.startMin.toInt() == 0){
            Toast.makeText(this, "Select a time for the schedule to start", Toast.LENGTH_SHORT).show()
            return true
        }
        if(Schedule.endMin.toInt() == 0 && Schedule.endHour.toInt() == 0){
            Toast.makeText(this, "Select a time for the schedule to end", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }


    private fun requestRequiredPermission(){
            requestPermission.launch(
                arrayOf(
                    //Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.SET_WALLPAPER
                )
            )
    }

    private fun showRationaleDialog(
        title: String,
        message: String
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
        builder.create().show()
    }

    private fun uriToDrawable(uri: Uri, context: Context): Drawable?{
        try{
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            return Drawable.createFromStream(inputStream, uri.toString())
        }
        catch(e: Exception){
            e.printStackTrace()
        }
        return null
    }

    private fun uriToBitmap(uri: Uri?, context: Context): Bitmap {

        val inputStream: InputStream? = uri?.let { context.contentResolver.openInputStream(it) }
        return BitmapFactory.decodeStream(inputStream)

    }


    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        val maxWidth = 1080
        val aspectRatio: Double = 9.0/16.0
        // Calculate the new height based on the maximum width and aspect ratio
        val newWidth = if (originalWidth > maxWidth) maxWidth else originalWidth
        val newHeight = (newWidth / aspectRatio).toInt()

        // Resize the bitmap
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun checkRunning(){

    }




}