package com.aaditx23.wallpaperwizard.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aaditx23.wallpaperwizard.components.clearCroppedPics
import com.aaditx23.wallpaperwizard.components.listFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("MutableCollectionMutableState")
@Composable
fun PicturesDirectory(){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var fileList by remember { mutableStateOf(mutableListOf<String>()) }
    var result by remember { mutableStateOf(false) }

    LaunchedEffect(result) {
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
            modifier = Modifier
                .padding(top = 70.dp)
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        result = clearCroppedPics(context)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete pictures",
                    modifier = Modifier
                        .size(50.dp)
                )
            }
            LazyColumn {
                items(fileList) {
                    Box(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(it)
                    }
                }
            }
        }
    }
    if (result) {
        Toast.makeText(context, "Cleared", Toast.LENGTH_SHORT).show()
    }
}