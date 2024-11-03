package com.aaditx23.wallpaperwizard.components

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.aaditx23.wallpaperwizard.ui.theme.palette2Plum
import com.aaditx23.wallpaperwizard.ui.theme.palette7Green1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun QuickSetCard(qsVM: QuickSetVM, quickSetItem: QuickSetModel) {
    var selectedHomeScreen by remember { mutableStateOf<Bitmap?>(null) }
    var selectedLockScreen by remember { mutableStateOf<Bitmap?>(null) }
    var showLockScreen by remember { mutableStateOf(false) }
    var setHomeScreen by remember { mutableStateOf<Boolean?>(null) }
    var setLockScreen by remember { mutableStateOf<Boolean?>(null) }
    var isLoading by remember { mutableStateOf(true) }
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
//                println(fileList)
                if(fileList.contains("home.jpg")){
                    selectedHomeScreen = JpgToBitmapAsync(context, "qs/$id/home.jpg")
                }
                if(fileList.contains("lock.jpg")){
                    selectedLockScreen = JpgToBitmapAsync(context, "qs/$id/lock.jpg")
                    showLockScreen = true
                }
            }
//            println(listSubfolders(context, "qs"))
            isLoading = false
        }
    }
//    println("ShowLockScreen $showLockScreen")

    ElevatedCard(
        onClick = {},
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        if(isLoading){
            CircularLoadingBasic("Loading...")
        }
        else{
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
                            if (!toggle && selectedLockScreen != null) {
                                scope.launch {
                                    val result = deleteImage(context, "qs/$id/lock.jpg")
                                    if (result) {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(
                                                context,
                                                "Deleted Lock Screen for $id",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    selectedLockScreen = null
                                }

                            }
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ImageCard(
                        setBitmap = { image ->
                            selectedHomeScreen = image
                            saveImage(context, image, "qs/$id", "home")
                        },
                        home = true,
                        loadedImage = selectedHomeScreen
                    )
                    if (showLockScreen) {
                        ImageCard(
                            setBitmap = { image ->
                                selectedLockScreen = image
                                saveImage(context, image, "qs/$id", "lock")
                            },
                            loadedImage = selectedLockScreen
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .height(200.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    isLoading = true
                                    if (selectedHomeScreen != null) {
                                        setHomeScreen = async {
                                            setWallpaper(
                                                context = context,
                                                bitmap = selectedHomeScreen!!,
                                                index = 0
                                            )
                                        }.await()
                                    }
                                    if(selectedLockScreen != null){
                                        setLockScreen = async {
                                            setWallpaper(
                                                context = context,
                                                bitmap = selectedLockScreen!!,
                                                index = 1
                                            )
                                        }.await()
                                    }
                                    isLoading = false

                                    if (setHomeScreen != null) {
                                        if(setHomeScreen!!){
                                            withContext(Dispatchers.Main){
                                                Toast.makeText(
                                                    context,
                                                    "Home Screen Wallpaper set successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        else {
                                            withContext(Dispatchers.Main){
                                                Toast.makeText(
                                                    context,
                                                    "There was an error setting Home Screen wallpapers",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        setHomeScreen = null
                                    }
                                    if (setLockScreen != null) {
                                        if(setLockScreen!!){
                                            withContext(Dispatchers.Main){
                                                Toast.makeText(
                                                    context,
                                                    "Lock Screen Wallpaper set successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        else {
                                            withContext(Dispatchers.Main){
                                                Toast.makeText(
                                                    context,
                                                    "There was an error setting Lock screen wallpaper",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        setLockScreen = null
                                    }

                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayCircle,
                                contentDescription = "Set Wallpaper",
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        IconButton(
                            onClick = {
                                scope.launch {
                                    deleteFolder(context, "qs/$id")
                                    qsVM.deleteQuickSet(quickSetItem._id)
//                                println(listSubfolders(context, "qs"))
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.DeleteForever,
                                contentDescription = "Delete QuickSet",
                                modifier = Modifier
                                    .size(50.dp)
                            )
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