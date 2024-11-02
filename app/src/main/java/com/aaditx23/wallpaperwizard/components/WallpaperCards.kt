package com.aaditx23.wallpaperwizard.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectedWallpaper(
    setBitmap: (Bitmap) -> Unit,
    text: String,
    loadedImage: Bitmap? = null
){
    val context = LocalContext.current
    val cardWidth = 100
    val cardHeight = getHeight(context, cardWidth) + 15
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
            .width(cardWidth.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSecondaryContainer)


    ) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column{
                if(selectedWallpaper != null){
                    Image(
                        bitmap = selectedWallpaper!!.asImageBitmap(),
                        contentDescription = "Current Wallpaper",
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
                Text(
                    text = text,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceDim
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
fun CurrentBitmap(image: Bitmap?, text: String) {
    val context = LocalContext.current
    val cardWidth = 100
    val cardHeight = getHeight(context, cardWidth) + 15
    ElevatedCard(
        onClick = {

        },
        modifier = Modifier
            .padding(10.dp)
            .height(cardHeight.dp)
            .width(cardWidth.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.inversePrimary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ){
            Column(

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
                Text(
                    text = text,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.scrim
                )
            }
        }
    }


}