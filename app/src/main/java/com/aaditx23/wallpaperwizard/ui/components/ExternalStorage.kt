package com.aaditx23.wallpaperwizard.ui.components

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

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

fun getCroppedStoragePath(context: Context): String {
    val dir = File(context.getExternalFilesDir(null), "Pictures")
    if (!dir.exists()) {
        dir.mkdirs() // ✅ make sure it exists
    }
    return dir.absolutePath
}

fun saveImage(context: Context, bitmap: Bitmap, fileName: String): String {
    val folderName = "Pictures"
    val externalDir = context.getExternalFilesDir(folderName) ?: return ""

    if (!externalDir.exists()) {
        externalDir.mkdirs()
    }

    val finalName = "$fileName.jpg"
    val file = File(externalDir, finalName)

    if (file.exists()) {
        file.delete()
    }

    return try {
        file.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        finalName
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}

