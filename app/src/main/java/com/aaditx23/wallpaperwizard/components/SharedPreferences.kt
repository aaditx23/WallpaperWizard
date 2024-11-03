package com.aaditx23.wallpaperwizard.components

import android.content.Context

fun savePref(context: Context, key: String, value: String) {
    val sharedPreferences = context.getSharedPreferences("WallpaperSchedulePrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().apply {
        putString(key, value)
        apply()
    }
}

fun getPref(context: Context, key: String): String {
    val sharedPreferences = context.getSharedPreferences("WallpaperSchedulePrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, "not_found")!!
}
