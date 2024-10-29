package com.aaditx23.wallpaperwizard.components

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.os.Build
import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

fun requestAllFilesAccess(context: Context) {
    if (!Environment.isExternalStorageManager()) {
        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        context.startActivity(intent)
    } else {
        // Permission is already granted, proceed with your logic
    }
}