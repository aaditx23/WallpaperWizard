package com.aaditx23.wallpaperwizard.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.wallpaperwizard.backend.models.QuickSetModel
import com.aaditx23.wallpaperwizard.backend.viewmodels.QuickSetVM
import com.aaditx23.wallpaperwizard.components.JpgToBitmapAsync
import com.aaditx23.wallpaperwizard.components.LockToggle
import com.aaditx23.wallpaperwizard.components.SelectedWallpaper
import com.aaditx23.wallpaperwizard.components.createFolder
import com.aaditx23.wallpaperwizard.components.deleteFolder
import com.aaditx23.wallpaperwizard.components.listFilesIn
import com.aaditx23.wallpaperwizard.components.listSubfolders
import com.aaditx23.wallpaperwizard.components.saveImage
import com.aaditx23.wallpaperwizard.components.setWallpaper
import com.aaditx23.wallpaperwizard.ui.theme.palette6LightIndigo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun QuickSetCard(qsVM: QuickSetVM, quickSetItem: QuickSetModel) {
    var selectedHomeScreen by remember { mutableStateOf<Bitmap?>(null) }
    var selectedLockScreen by remember { mutableStateOf<Bitmap?>(null) }
    var showLockScreen by remember { mutableStateOf(false) }
    var hasLockScreen by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val id = quickSetItem._id.toHexString()

    LaunchedEffect(quickSetItem) {
        scope.launch {
            val dirList = listSubfolders(context, "qs")
            if(!dirList.contains(id)){
                createFolder(context, "qs/$id")
            }
            else{
                val fileList = listFilesIn(context, "qs/$id")
                println(fileList)
                if(fileList.contains("home.jpg")){
                    selectedHomeScreen = JpgToBitmapAsync(context, "qs/$id/home.jpg")
                }
                if(fileList.contains("lock.jpg")){
                    selectedLockScreen = JpgToBitmapAsync(context, "qs/$id/lock.jpg")
                    showLockScreen = true
                }
            }
            println(listSubfolders(context, "qs"))
        }
    }
    println("ShowLockScreen $showLockScreen")

    ElevatedCard(
        onClick = {},
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(palette6LightIndigo),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                LockToggle(
                    hasLock = showLockScreen,
                    set = { toggle ->
                        showLockScreen = toggle
                        if(!toggle && selectedLockScreen != null){

                        }
                    }
                )
            }
            Row {
                SelectedWallpaper(
                    setBitmap = { image ->
                        selectedHomeScreen = image
                        saveImage(context, image, "qs/$id", "home")
                    },
                    text = "Selected Home",
                    loadedImage = selectedHomeScreen
                )
                if (showLockScreen) {
                    SelectedWallpaper(
                        setBitmap = { image ->
                            selectedLockScreen = image
                            saveImage(context, image, "qs/$id", "lock")
                        },
                        text = "Selected Lock",
                        loadedImage = selectedLockScreen
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(170.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    CardButton(
                        text = "Set Wallpaper(s)",
                        buttonColor = MaterialTheme.colorScheme.primary
                    ) {
                        scope.launch {
                            if(selectedHomeScreen != null){
                                val resultHome = async{
                                    setWallpaper(
                                        context = context,
                                        bitmap = selectedHomeScreen!!,
                                        index = 0
                                    )
                                }.await()
                                val resultLock = async{
                                    setWallpaper(
                                        context = context,
                                        bitmap = selectedLockScreen!!,
                                        index = 1
                                    )
                                }.await()
                                withContext(Dispatchers.Main){
                                    if (resultHome && resultLock){
                                        Toast.makeText(context, "Wallpapers set successfully", Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        Toast.makeText(context, "There was an error setting wallpapers", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    CardButton(
                        text = "Delete QuickSet",
                        buttonColor = MaterialTheme.colorScheme.error
                    ) {
                        scope.launch {
                            qsVM.deleteQuickSet(quickSetItem._id)
                            deleteFolder(context, "qs/$id")
                            println(listSubfolders(context, "qs"))
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun CardButton(
    text: String,
    buttonColor: Color,
    textColor: Color = Color.White,
    onClick: () -> Unit
){
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .width(150.dp)
            .height(50.dp),
        colors = CardDefaults.cardColors(buttonColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = text,
                color = textColor,
                fontSize = 15.sp
            )
        }
    }
}