package com.aaditx23.wallpaperwizard.components

import android.app.WallpaperManager
import android.content.Context
import android.graphics.drawable.Drawable


fun getCurrentWallpaperDrawable(context: Context): Drawable? {
    val wallpaperManager = WallpaperManager.getInstance(context)
    val wallpaperDrawable: Drawable? = wallpaperManager.getDrawable(WallpaperManager.FLAG_SYSTEM)
    return wallpaperDrawable
}