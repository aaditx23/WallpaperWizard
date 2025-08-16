package com.aaditx23.wallpaperwizard.ui.components

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.drawable.Drawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException

val flags = listOf(
    WallpaperManager.FLAG_SYSTEM,
    WallpaperManager.FLAG_LOCK
)
suspend fun getCurrentDrawable(
    context: Context,
    index: Int): Drawable? = withContext(Dispatchers.IO) {
    val wallpaperManager = WallpaperManager.getInstance(context)
    // Ensure `flags` array is defined, as the function is referencing it
    val wallpaperDrawable: Drawable? = wallpaperManager.getDrawable(flags[index])
    wallpaperDrawable
}



@SuppressLint("ObsoleteSdkInt")
suspend fun setWallpaper(
    context: Context,
    bitmap: Bitmap,
    index: Int,
    name: String = ""
): Boolean = withContext(Dispatchers.IO) {
    val wallpaperManager = WallpaperManager.getInstance(context)
        try {
            if(name != ""){
                val convertedBitmap = BitmapFactory.decodeFile(name)
                wallpaperManager.setBitmap(convertedBitmap, null, true, flags[index])
            }

            true // Success
        } catch (e: IOException) {
            e.printStackTrace()
            false // Failure
        }

}

@SuppressLint("ObsoleteSdkInt")
suspend fun setWallpaper(
    context: Context,
    index: Int,
    name: String = ""
): Boolean = withContext(Dispatchers.IO) {
    val wallpaperManager = WallpaperManager.getInstance(context)
    try {
        if(name != ""){
            println("File is : $name")
            val convertedBitmap = BitmapFactory.decodeFile(name)
            wallpaperManager.setBitmap(convertedBitmap, null, true, flags[index])
        }

        true // Success
    } catch (e: IOException) {
        e.printStackTrace()
        false // Failure
    }

}

