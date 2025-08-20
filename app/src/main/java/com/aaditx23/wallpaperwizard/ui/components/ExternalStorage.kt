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
fun getPrevStoragePath(context: Context): String {
    val dir = File(context.getExternalFilesDir(null), "prev")
    if (!dir.exists()) {
        dir.mkdirs() // ✅ make sure it exists
    }
    return dir.absolutePath
}

suspend fun savePrev(context: Context, bitmap: Bitmap, fileName: String): String {
    val folderName = "prev"
    val externalDir = context.getExternalFilesDir(folderName) ?: return ""

    println("EXTERNAL FOLDER NAME: $externalDir")
    if (!externalDir.exists()) {
        externalDir.mkdirs()
    }

    val finalName = "${fileName}_${System.currentTimeMillis()}.jpg"
    val file = File(externalDir, finalName)

    externalDir.listFiles()?.forEach { file ->
        if (file.name.contains(fileName, ignoreCase = true)) {
            println("Deleting ${file.name}")
            file.delete()
        }
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

