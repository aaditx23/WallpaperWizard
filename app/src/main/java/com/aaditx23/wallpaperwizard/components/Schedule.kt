package com.aaditx23.wallpaperwizard.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.aaditx23.wallpaperwizard.components.scheduler.WallpaperScheduler
import kotlinx.coroutines.launch


@SuppressLint("MutableCollectionMutableState")
@Composable
fun Schedule(){
    var currentHomeScreen by remember { mutableStateOf<Bitmap?>(null) }
    var currentLockScreen by remember { mutableStateOf<Bitmap?>(null) }
    var selectedHomeScreen by remember { mutableStateOf<Bitmap?>(null) }
    var selectedLockScreen by remember { mutableStateOf<Bitmap?>(null) }
    val emptyTime = "00:00 AM"
    var startTime by remember { mutableStateOf("00:00 AM") }
    var endTime by remember { mutableStateOf("00:00 AM") }
    var startTime12H by remember { mutableStateOf("00:00 AM") }
    var endTime12H by remember { mutableStateOf("00:00 AM") }
    var showLockScreen by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val cardWidth = 100
    val wallpaperScheduler = WallpaperScheduler(context)
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
    LazyColumn(
        modifier = Modifier
            .height((LocalConfiguration.current.screenHeightDp-45).dp)
            .padding(top = 70.dp)
    ) {

        item{
            Column {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 50.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // previous home
                    SelectedWallpaper(
                        setBitmap = {},
                        home = true,
                        loadedImage = currentHomeScreen,
                        width = cardWidth,
                        text = "Previous Home",
                        cardColor = MaterialTheme.colorScheme.inversePrimary,
                        iconTint = if (currentHomeScreen != null) MaterialTheme.colorScheme.inversePrimary
                        else MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    if (showLockScreen) {
                        // previous lock
                        SelectedWallpaper(
                            setBitmap = { bitmap ->
                                currentLockScreen = bitmap
                            },
                            home = false,
                            loadedImage = currentLockScreen,
                            width = cardWidth,
                            text = "Previous Lock",
                            cardColor = MaterialTheme.colorScheme.inversePrimary,
                            iconTint = if (currentLockScreen != null) MaterialTheme.colorScheme.inversePrimary
                            else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    } else {
                        // selected home
                        SelectedWallpaper(
                            setBitmap = { bitmap ->
                                selectedHomeScreen = bitmap
                            },
                            home = true,
                            width = cardWidth
                        )
                    }
                }
                if (showLockScreen) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 50.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // selected home
                        SelectedWallpaper(
                            setBitmap = { bitmap ->
                                selectedHomeScreen = bitmap
                            },
                            home = true,
                            width = cardWidth
                        )
                        // selected lock
                        SelectedWallpaper(
                            setBitmap = { bitmap ->
                                selectedLockScreen = bitmap
                            },
                            width = cardWidth
                        )
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
                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text =
                                if (showLockScreen) "Lock & Home"
                                else "Home",
                                fontSize = 15.sp
                            )
//                        DayBar(
//                            selectedIndex = daySelected
//                        ) { i ->
//                            daySelected.add(i)
//                        }
                            LockToggle(
                                hasLock = showLockScreen,
                                set = { check ->
                                    showLockScreen = check
                                }
                            )
                            Row {
                                Column(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth(0.5f)
                                ) {
                                    TimeField("Start", startTime12H) { temp, time ->
                                        startTime = temp
                                        startTime12H = time
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    TimeField("End", endTime12H) { temp, time ->
                                        endTime = temp
                                        endTime12H = time
                                    }
                                }
                                println("Start time $startTime end time $endTime")
                                Column(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                ) {
                                    Button(
                                        onClick = {
                                            wallpaperScheduler.scheduleAlarm(
                                                startTimeString = startTime,
                                                endTimeString = endTime
                                            )
                                        },
                                        enabled = (startTime != emptyTime && endTime != emptyTime)
                                    ) {
                                        Text("Start")
                                    }
                                    Button(
                                        onClick = {
                                            wallpaperScheduler.cancelAlarm()
                                        }
                                    ) {
                                        Text("Stop")
                                    }
                                    Button(
                                        onClick = {
                                            startTime = emptyTime
                                            endTime = emptyTime
                                            startTime12H = emptyTime
                                            endTime12H = emptyTime
                                        }
                                    ) {
                                        Text("Clear")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Repeat days



}

