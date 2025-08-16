package com.aaditx23.wallpaperwizard.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun RecentImages(
    onImagePicked: (name: String) -> Unit
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var fileList by remember { mutableStateOf(mutableListOf<String>()) }
    var result by remember { mutableStateOf(false) }
    val path = getCroppedStoragePath(context)
    var showPicker by remember { mutableStateOf(false) }
    LaunchedEffect(result, showPicker) {
        scope.launch {
            fileList = listFiles(context, "Pictures").toMutableList()
        }
    }

    if(fileList.isEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text("No Cropped Pictures in Cache")
        }
    }
    else{
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                modifier = Modifier
                    .padding(top = 70.dp)
            ) {
                items(fileList) { image ->
                    println("IMAGE IS: $path/$image")
                    ElevatedCard(
                        modifier = Modifier
                            .width(100.dp),
                        onClick = {
                            onImagePicked(image)
                        }
                    ) {
                        GlideImage(
                            model = "$path/$image",
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .clip(RoundedCornerShape(10.dp)),
                        ) {
                            it
                                .thumbnail(
                                    it.clone()
                                        .load("${getCroppedStoragePath(context)}/$image")
                                        .override(150)

                                )
                        }
                    }

                }
            }

            Button(
                modifier = Modifier
                    .padding(vertical = 5.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                onClick = { showPicker = !showPicker }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Add")
                    Icon(
                        imageVector = Icons.Filled.AddAPhoto,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .size(16.dp),
                    )
                }
            }
        }
    }

    if(showPicker){
        ImagePicker { image, name ->
            onImagePicked(name)
            showPicker = false
        }
    }



    if (result) {
        Toast.makeText(context, "Cleared", Toast.LENGTH_SHORT).show()
    }
}