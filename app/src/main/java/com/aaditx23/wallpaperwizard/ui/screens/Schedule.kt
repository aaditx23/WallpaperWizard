package com.aaditx23.wallpaperwizard.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.aaditx23.wallpaperwizard.components.CurrentBitmap
import com.aaditx23.wallpaperwizard.components.ImagePicker
import com.aaditx23.wallpaperwizard.components.LockToggle
import com.aaditx23.wallpaperwizard.components.SelectedWallpaper
import com.aaditx23.wallpaperwizard.components.TimeField
import com.aaditx23.wallpaperwizard.components.TimePicker
import com.aaditx23.wallpaperwizard.components.getCurrentDrawable
import com.aaditx23.wallpaperwizard.components.to12Hour
import kotlinx.coroutines.launch


@SuppressLint("MutableCollectionMutableState")
@Composable
fun Schedule(){
    var currentHomeScreen by remember { mutableStateOf<Bitmap?>(null) }
    var currentLockScreen by remember { mutableStateOf<Bitmap?>(null) }
    var selectedHomeScreen by remember { mutableStateOf<Bitmap?>(null) }
    var selectedLockScreen by remember { mutableStateOf<Bitmap?>(null) }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }


    var showLockScreen by remember { mutableStateOf(false) }
    var setStartTime by remember { mutableStateOf(false) }
    var setEndTime by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(showLockScreen) {
        scope.launch{
            getCurrentDrawable(context, 0)?.let {
                currentHomeScreen = it.toBitmap()
            }
            if(showLockScreen){
                getCurrentDrawable(context, 1)?.let {
                    currentLockScreen = it.toBitmap()
                }
            }
            else{
                currentLockScreen = null
            }
        }
    }
    Box(
        modifier = Modifier
//            .fillMaxSize()
            .padding(top = 70.dp)
    ) {
        Column{
            Row {
                Column {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        CurrentBitmap(currentHomeScreen, "Current Home")
                        if (showLockScreen) {
                            CurrentBitmap(currentLockScreen, "Current Lock")
                        } else {
                            SelectedWallpaper(
                                setBitmap = { bitmap ->
                                    selectedHomeScreen = bitmap
                                },
                                text = "Selected Home"
                            )
                        }
                    }
                    if (showLockScreen) {
                        Row(
                            modifier = Modifier,
//                        .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            SelectedWallpaper(
                                setBitmap = { bitmap ->
                                    selectedHomeScreen = bitmap
                                },
                                text = "Selected Home"
                            )
                            SelectedWallpaper(
                                setBitmap = { bitmap ->
                                    selectedLockScreen = bitmap
                                },
                                text = "Selected Lock"
                            )
                        }
                    }
                }
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.height(78.dp))
                            Text(
                                text =
                                if (showLockScreen) "Lock & Home"
                                else "Home",
                                fontSize = 15.sp
                            )
                            LockToggle { check ->
                                showLockScreen = check
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            TimeField("Start") { time ->
                                startTime = time
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            TimeField("End") { time ->
                                endTime = time
                            }
                        }
                    }
                }
            }

            // Repeat days


        }
    }
}

