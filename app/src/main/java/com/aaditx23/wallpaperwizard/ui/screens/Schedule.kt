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
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap


@Composable
fun CurrentWallpaperImage() {
    // Get the application context
    val context = LocalContext.current

    // State to hold the wallpaper bitmap
    var wallpaperBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Fetch the wallpaper in a LaunchedEffect
    LaunchedEffect(Unit) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        val wallpaperDrawable: Drawable? = wallpaperManager.getDrawable(WallpaperManager.FLAG_SYSTEM)
        wallpaperDrawable?.let { drawable ->
            wallpaperBitmap = drawable.toBitmap()
        }
    }

    // Display the wallpaper if available
    wallpaperBitmap?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Current Wallpaper"
        )
    }
}