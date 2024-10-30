package com.aaditx23.wallpaperwizard.components

import android.app.WallpaperManager
import android.content.Context
import android.graphics.drawable.Drawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val flags = listOf(
    WallpaperManager.FLAG_SYSTEM,
    WallpaperManager.FLAG_LOCK
)
suspend fun getCurrentDrawable(context: Context, index: Int): Drawable? = withContext(Dispatchers.IO) {
    val wallpaperManager = WallpaperManager.getInstance(context)
    // Ensure `flags` array is defined, as the function is referencing it
    val wallpaperDrawable: Drawable? = wallpaperManager.getDrawable(flags[index])
    wallpaperDrawable
}
