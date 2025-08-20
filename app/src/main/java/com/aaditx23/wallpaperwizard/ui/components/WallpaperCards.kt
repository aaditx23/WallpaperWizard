package com.aaditx23.wallpaperwizard.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ComposableImageOptions
import java.io.File


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageCard(
    setImageName: (String) -> Unit,
    home: Boolean = false,
    loadedImageString: String = "",
    id: String = "",
    width: Int = 100,
    text: String = "",
    cardColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    iconTint: Color = MaterialTheme.colorScheme.inversePrimary,
    loading: Boolean = false
){
    println("LOADD IMAGE PATH: $loadedImageString")
    val context = LocalContext.current
    val cardHeight = getHeight(context, width)
    var showImagePicker by remember { mutableStateOf(false) }
    var selectedWallpaperString by remember{ mutableStateOf(loadedImageString) }
    var iconColor by remember { mutableStateOf(iconTint) }

    LaunchedEffect(loadedImageString) {
        if(loadedImageString.isNotEmpty() && !loadedImageString.endsWith("/")){
            selectedWallpaperString = loadedImageString
        }
    }
    LaunchedEffect(selectedWallpaperString) {
        showImagePicker = false
    }
    if(!text.split(" ").contains("Previous")){
        iconColor = if (selectedWallpaperString.isEmpty() || selectedWallpaperString.endsWith("/")) MaterialTheme.colorScheme.inversePrimary
        else MaterialTheme.colorScheme.onSecondaryContainer
    }

    Column{
        if(showImagePicker){
            GeneralDialog(
                onCancel = { showImagePicker = false },
                content = {
                    RecentImages(
                        onImagePicked = {name ->
                            setImageName(name)
                            selectedWallpaperString = name
                            showImagePicker = false
                        }
                    )
                }
            )

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
            colors = CardDefaults.cardColors(cardColor)


        ) {
            val validImagePath = remember(selectedWallpaperString) {
                println("Validating path: $selectedWallpaperString")
                val file = File(selectedWallpaperString)
                if (file.exists() && file.isFile) file.absolutePath else null
            }


            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                if (loading || validImagePath == null || selectedWallpaperString.endsWith("/")) {
                    println("LOADING")
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
                else{
                    GlideImage(
                        model = File(selectedWallpaperString),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(RoundedCornerShape(10.dp)),
                    )
                    {
                        it
                            .thumbnail(
                                it.clone()
                                    .load(selectedWallpaperString)
                                    .override(100, cardHeight)
                                    .signature(ObjectKey(selectedWallpaperString))
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                            )
                    }
                }
                Icon(
                    imageVector =
                        if (home) Icons.Outlined.Home
                        else Icons.Outlined.Lock,
                    contentDescription = "Home",
                    modifier = Modifier
                        .size((width - 10).dp)
                        .alpha(0.7f),
                    tint = iconColor
                )

            }
        }
        if(text.isNotEmpty()){
            Text(
                text,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .width(width.dp),
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }
    }

}
