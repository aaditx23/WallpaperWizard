package com.aaditx23.wallpaperwizard.ui.screens

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.aaditx23.wallpaperwizard.components.ImagePicker
import com.aaditx23.wallpaperwizard.components.createFolder
import com.aaditx23.wallpaperwizard.components.deleteFolder
import com.aaditx23.wallpaperwizard.components.getCurrentWallpaperDrawable
import com.aaditx23.wallpaperwizard.components.listFolders


@Composable
fun Schedule(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
    ) {
        Column {
            Row(
            ) {
                CurrentWallpaper()
                SelectedWallpaper()
            }
        }
    }
}

@Composable
fun SelectedWallpaper(){
    val context = LocalContext.current
    var selectedWallpaper by remember{ mutableStateOf<Bitmap?>(null) }
    var showImagePicker by remember { mutableStateOf(false) }
    if(selectedWallpaper == null){
        Card(
            onClick = {
                showImagePicker = !showImagePicker
            },
            modifier = Modifier
                .size(200.dp)
        ) { }
    }
    else{
        Image(
            bitmap = selectedWallpaper!!.asImageBitmap(),
            contentDescription = "Selected Wallpaper",
            modifier = Modifier
                .size(200.dp)
        )
    }

    if (showImagePicker){
        ImagePicker { image ->
            selectedWallpaper = image
            showImagePicker = false
        }
    }
}

@Composable
fun CurrentWallpaper() {
    // Get the application context
    val context = LocalContext.current

    // State to hold the wallpaper bitmap
    var wallpaperBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Fetch the wallpaper in a LaunchedEffect
    LaunchedEffect(Unit) {
        getCurrentWallpaperDrawable(context)?.let {
            wallpaperBitmap = it.toBitmap()
        }
    }

    // Display the wallpaper if available
    wallpaperBitmap?.let { bitmap ->
        ElevatedCard(
            onClick = {

            }
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Current Wallpaper",
                modifier = Modifier
                    .size(200.dp)
            )
        }
    }
}