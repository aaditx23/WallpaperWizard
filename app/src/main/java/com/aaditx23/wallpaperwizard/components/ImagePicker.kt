package com.aaditx23.wallpaperwizard.components

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Composable
fun ImagePicker(
    onImagePicked: (image: Bitmap) -> Unit
){
    println("Launching")
    var selectedImageBitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var showGallery by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    var hasImagePermission by remember{ mutableStateOf(false) }
    val permission = Manifest.permission.READ_MEDIA_IMAGES

    hasImagePermission = permissionLauncher(
        context = context,
        permission = permission
    )

    val temp = getScreenMetrics(context)
    val screenX = temp[0]
    val screenY = temp[1]

    val cropLauncher = rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
        if (result.isSuccessful){
            val selectedImageUri = result.uriContent
            val bitmap = createBitmapFromUri(context, selectedImageUri!!)
            selectedImageBitmap = bitmap
            onImagePicked(bitmap)
        } else {
            Toast.makeText(context, "An Error occurred while cropping", Toast.LENGTH_SHORT).show()
            println("${result.error}")
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        val cropOptions = CropImageContractOptions(uri, CropImageOptions().apply {
            fixAspectRatio = true
            aspectRatioX = screenX
            aspectRatioY = screenY
            outputCompressQuality = 100
            outputCompressFormat = Bitmap.CompressFormat.JPEG
        })
        cropLauncher.launch(cropOptions)
    }

    if (hasImagePermission){
        if(!showGallery){
            showGallery = true
            println("LAUNCHING")
            launcher.launch(("image/*"))
        }
    }
}

private fun getScreenMetrics(context: Context): List<Int>  {
    val display = context.resources.displayMetrics

    val width = display.widthPixels
    val height = display.heightPixels

    return listOf( width, height)
}
fun getHeight(context: Context, width: Int): Int {
    // Get the screen's width and height to calculate the aspect ratio
    val screenMetrics = getScreenMetrics(context)

    // Calculate the aspect ratio (height-to-width ratio)
    val aspectRatio = screenMetrics[1].toFloat() / screenMetrics[0].toFloat()

    // Calculate the height based on the given width and aspect ratio
    return (width * aspectRatio).toInt()
}

fun createBitmapFromUri(context: Context, uri: Uri): Bitmap {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    return BitmapFactory.decodeStream(inputStream)
}

//fun bitmapToString(bitmap: Bitmap): String {
//    val outputStream = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//    val byteArray = outputStream.toByteArray()
//    return Base64.encodeToString(byteArray, Base64.DEFAULT)
//}

suspend fun bitmapToString(bitmap: Bitmap, maxSizeKB: Int = 100): String = withContext(Dispatchers.IO) {
    var compressedBitmap = bitmap
    var quality = 100
    var outputStream = ByteArrayOutputStream()

    // Compress and check size
    do {
        outputStream.reset() // Reset the stream to clear previous data
        compressedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val byteArray = outputStream.toByteArray()
        val sizeKB = byteArray.size / 1024
        println("Size of image $sizeKB")
        quality -= 10 // Reduce quality for further compression if needed

    } while (sizeKB > maxSizeKB && quality > 0)

    Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
}

