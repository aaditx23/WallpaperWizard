package com.aaditx23.wallpaperwizard.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

fun saveImage(context: Context, bitmap: Bitmap, folderName: String, fileName: String): String? {
    // Get the internal storage directory of the app
    val appInternalDir = context.filesDir

    // Create the target folder path
    val folderPath = File(appInternalDir, folderName)

    // Ensure the folder exists
    if (!folderPath.exists()) {
        folderPath.mkdirs() // Create the folder if it doesn't exist
    }

    // Create the file for the image
    val file = File(folderPath, "$fileName.jpg")

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
fun listSubfolders(context: Context, folderName: String): List<String> {
    // Get the internal storage directory of the app
    val appInternalDir = context.filesDir

    // Create a reference to the specified folder
    val targetDir = File(appInternalDir, folderName)

    // Check if the specified folder exists and is indeed a directory
    return if (targetDir.exists() && targetDir.isDirectory) {
        // List and filter only directories within the specified folder
        targetDir.listFiles()?.filter { it.isDirectory }?.map { it.name } ?: emptyList()
    } else {
        // Return an empty list if the folder does not exist
        emptyList()
    }
}


fun listFilesIn(context: Context, folderName: String): List<String> {
    // Get the internal storage directory of the app
    val appInternalDir = context.filesDir

    // Create a reference to the specified folder
    val folderPath = File(appInternalDir, folderName) // Use the context to get the app's internal storage directory

    // Check if the specified folder exists and is indeed a directory
    if (folderPath.exists() && folderPath.isDirectory) {
        return folderPath.listFiles()?.map { it.name } ?: emptyList()
    }
    return emptyList() // Return an empty list if folder doesn't exist or isn't a directory
}

suspend fun JpgToBitmapAsync(context: Context, fullPath: String): Bitmap? {
    return withContext(Dispatchers.IO) { // Switch to the IO context
        // Get the internal storage directory of the app
        println("Converting")
        val appInternalDir = context.filesDir
        val file = File(appInternalDir, fullPath) // Create the full path using context.filesDir

        if (file.exists()) {
            println("Ase")
            BitmapFactory.decodeFile(file.absolutePath) // Decode the file to a Bitmap
        } else {
            println("Nai")
            null
        }
    }
}


