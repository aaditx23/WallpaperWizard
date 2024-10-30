package com.aaditx23.wallpaperwizard.components

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.IOException


val path = "/data/user/0/com.aaditx23.wallpaperwizard/files/"

fun createFolder(context: Context, folderName: String): String {
    val folder = File(context.filesDir, folderName)
    if (!folder.exists()) {
        folder.mkdirs()
    }
    return folder.absolutePath
}

fun saveImage(bitmap: Bitmap, path: String, fileName: String): String? {
    val file = File(path, "$fileName.jpg")
    return try {
        file.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        file.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun deleteFolder(context: Context, folderName: String): Boolean {
    val folder = File(context.filesDir, folderName)
    if (folder.exists() && folder.isDirectory) {
        return folder.deleteRecursively()
    }
    return false
}

fun deleteImage(context: Context, folderName: String, fileName: String): Boolean {
    val folder = File(context.filesDir, folderName)
    val file = File(folder, "$fileName.jpg")
    return if (file.exists() && file.isFile) {
        file.delete()
    } else {
        false
    }
}

fun listFolders(context: Context): List<String> {
    // Get the internal storage directory of the app
    val appInternalDir = context.filesDir

    // Filter to get only directories within the internal storage
    return appInternalDir.listFiles()?.filter { it.isDirectory }?.map { it.name } ?: emptyList()
}

