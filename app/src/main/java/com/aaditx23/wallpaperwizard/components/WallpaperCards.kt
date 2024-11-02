package com.aaditx23.wallpaperwizard.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun SelectedWallpaper(
    setBitmap: (Bitmap) -> Unit,
    home: Boolean = false,
    loadedImage: Bitmap? = null,
    width: Int = 100
){
    val context = LocalContext.current
    val cardHeight = getHeight(context, width)
    var showImagePicker by remember { mutableStateOf(false) }
    var selectedWallpaper by remember{ mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(loadedImage) {
        if(loadedImage!= null){
            selectedWallpaper = loadedImage
        }
    }

    ElevatedCard(
        onClick = {
            showImagePicker = true
        },
        modifier = Modifier
            .padding(10.dp)
            .height(cardHeight.dp)
            .width(width.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSecondaryContainer)


    ) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ){
                if(selectedWallpaper != null){
                    Image(
                        bitmap = selectedWallpaper!!.asImageBitmap(),
                        contentDescription = "Current Wallpaper",
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
                Icon(
                    imageVector =
                        if(home) Icons.Outlined.Home
                        else Icons.Outlined.Lock,
                    contentDescription = "Home",
                    modifier = Modifier
                        .size((width - 10).dp)
                        .alpha(0.7f),
                    tint =
                    if(loadedImage == null) MaterialTheme.colorScheme.inversePrimary
                    else MaterialTheme.colorScheme.onSecondaryContainer
                )

            }
        }

    }



    if (showImagePicker ){
        ImagePicker { image ->
            selectedWallpaper = image
            setBitmap(image)
            showImagePicker = false
        }
    }
}

@Composable
fun CurrentBitmap(
    image: Bitmap?,
    home: Boolean = false,
    width: Int = 100
) {
    val context = LocalContext.current
    val cardHeight = getHeight(context, width)
    ElevatedCard(
        onClick = {

        },
        modifier = Modifier
            .padding(10.dp)
            .height(cardHeight.dp)
            .width(width.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.inversePrimary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ){
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ){
                image?.let{
                    Image(
                        bitmap = image.asImageBitmap(),
                        contentDescription = "Current Wallpaper",
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
                Icon(
                    imageVector =
                        if(home) Icons.Outlined.Home
                        else Icons.Outlined.Lock,
                    contentDescription = "Home",
                    modifier = Modifier
                        .size((width - 10).dp)
                        .alpha(0.8f),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }


}