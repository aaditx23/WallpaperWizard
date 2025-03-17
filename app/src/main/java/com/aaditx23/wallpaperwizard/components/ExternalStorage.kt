package com.aaditx23.wallpaperwizard.components

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun clearCroppedPics(context: Context): Boolean = withContext(Dispatchers.IO) {
    val picturesFolder = File(context.getExternalFilesDir(null), "Pictures")
    if (picturesFolder.exists() && picturesFolder.isDirectory) {
        val files = picturesFolder.listFiles()
        files?.forEach { file ->
            file.delete()
        }
        true
    } else {
        false
    }
}

suspend fun listFiles(context: Context, folderName: String): List<String> = withContext(Dispatchers.IO) {
    val externalDir = context.getExternalFilesDir(null)
    val folderPath = File(externalDir, folderName)

    if (folderPath.exists() && folderPath.isDirectory) {
        folderPath.listFiles()?.map { it.name } ?: emptyList()
    } else {
        emptyList()
    }
}
