package com.aaditx23.wallpaperwizard.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import com.aaditx23.wallpaperwizard.backend.models.ScheduleModel
import com.aaditx23.wallpaperwizard.backend.viewmodels.ScheduleVM
import com.aaditx23.wallpaperwizard.components.scheduler.WallpaperScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@SuppressLint("MutableCollectionMutableState")
@Composable
fun Schedule(
    schedule: ScheduleModel,
    schedulevm: ScheduleVM,
    wallpaperScheduler: WallpaperScheduler,
    navController: NavHostController
){
    var prevHomeScreen by remember { mutableStateOf<Bitmap?>(null) }
    var prevLockScreen by remember { mutableStateOf<Bitmap?>(null) }
    var selectedHomeScreen by remember { mutableStateOf<Bitmap?>(null) }
    var selectedLockScreen by remember { mutableStateOf<Bitmap?>(null) }
    val emptyTime = "00:00 AM"
    var startTime by remember { mutableStateOf("00:00 AM") }
    var endTime by remember { mutableStateOf("00:00 AM") }
    var startTime12H by remember { mutableStateOf("00:00 AM") }
    var endTime12H by remember { mutableStateOf("00:00 AM") }
    var showLockScreen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val cardWidth = 100
    val id = schedule._id.toHexString()
    var status by remember { mutableStateOf("") }
    LaunchedEffect(schedule) {
        scope.launch {
            val temp_id = getPref(context, "schedule_id")
            if(temp_id == id){
                status = getPref(context, "schedule_status")
            }
            val dirList = listSubfolders(context, "schedule")
            if(!dirList.contains(id)){
                createFolder(context, "schedule/$id")
            }
            else{
                val fileList = listFilesIn(context, "schedule/$id")
                println(fileList)
                if(fileList.contains("prevHome.jpg")){
                    prevHomeScreen = JpgToBitmapAsync(context, "schedule/$id/prevHome.jpg")
                }
                if(fileList.contains("prevLock.jpg")){
                    prevLockScreen = JpgToBitmapAsync(context, "schedule/$id/prevLock.jpg")
                    showLockScreen = true
                }
                if(fileList.contains("selectedHome.jpg")){
                    selectedHomeScreen = JpgToBitmapAsync(context, "schedule/$id/selectedHome.jpg")
                }
                if(fileList.contains("selectedLock.jpg")){
                    selectedLockScreen = JpgToBitmapAsync(context, "schedule/$id/selectedLock.jpg")
                }
            }
            println(listSubfolders(context, "schedule"))
            schedule.startTime?.let {
                startTime = it
                startTime12H = to12HourString(it)
            }
            schedule.endTime?.let {
                endTime = it
                endTime12H = to12HourString(it)
            }
            if(prevHomeScreen == null && status != "started" && status != "scheduled"){
                getCurrentDrawable(context, 0)?.let {
                    prevHomeScreen = it.toBitmap()
                    saveImage(context, it.toBitmap(), "schedule/$id", "prevHome")
                }
            }
            status = getPref(context, "schedule_status")
            println("Status from prefs $status")
            schedulevm.setRunning(schedule._id, status)
            isLoading = false
        }
    }
    LaunchedEffect(showLockScreen) {
        scope.launch{
            isLoading = true
            if(showLockScreen){
                if(prevLockScreen == null){
                    getCurrentDrawable(context, 1)?.let {
                        prevLockScreen = it.toBitmap()
                        saveImage(context, it.toBitmap(), "schedule/$id", "prevLock")
                    }
                }
            }
            else{
                if (prevLockScreen != null) {
                    scope.launch {
                        val prevResult = deleteImage(context, "schedule/$id/prevLock.jpg")
                        var selectedResult = false
                        if(selectedLockScreen != null){
                            selectedResult = deleteImage(context, "schedule/$id/selectedLock.jpg")
                        }
                        delay(100)
                        if (prevResult && selectedResult) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Deleted Lock Screen for $id",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        prevLockScreen = null
                        selectedLockScreen = null
                    }

                }
            }
            isLoading = false
        }
    }

    println("Status $status")
    fun clear(){
        scope.launch{
            deleteFolder(context, "schedule/$id")
            val dirList =
                listSubfolders(context, "schedule")
            if (!dirList.contains(id)) {
                createFolder(context, "schedule/$id")
            }
            startTime = emptyTime
            endTime = emptyTime
            startTime12H = emptyTime
            endTime12H = emptyTime
            prevHomeScreen = null
            prevLockScreen = null
            selectedHomeScreen = null
            selectedLockScreen = null
            schedulevm.setStartTime(schedule._id, emptyTime)
            schedulevm.setEndTime(schedule._id, emptyTime)
        }
    }

    if(isLoading){
        CircularLoadingBasic("Loading...")
    }
    else{
        LazyColumn(
            modifier = Modifier
                .height((LocalConfiguration.current.screenHeightDp - 45).dp)
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
                        ImageCard(
                            setBitmap = { bitmap ->
                                prevHomeScreen = bitmap
                                saveImage(context, bitmap, "schedule/$id", "prevHome")
                            },
                            home = true,
                            loadedImage = prevHomeScreen,
                            width = cardWidth,
                            text = "Previous Home",
                            cardColor = MaterialTheme.colorScheme.inversePrimary,
                            iconTint = if (prevHomeScreen != null) MaterialTheme.colorScheme.inversePrimary
                            else MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        if (showLockScreen) {
                            // previous lock
                            ImageCard(
                                setBitmap = { bitmap ->
                                    prevLockScreen = bitmap
                                    saveImage(context, bitmap, "schedule/$id", "prevLock")
                                },
                                home = false,
                                loadedImage = prevLockScreen,
                                width = cardWidth,
                                text = "Previous Lock",
                                cardColor = MaterialTheme.colorScheme.inversePrimary,
                                iconTint = if (prevLockScreen != null) MaterialTheme.colorScheme.inversePrimary
                                else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        } else {
                            // selected home
                            ImageCard(
                                setBitmap = { bitmap ->
                                    selectedHomeScreen = bitmap
                                    saveImage(context, bitmap, "schedule/$id", "selectedHome")
                                },
                                home = true,
                                width = cardWidth,
                                loadedImage = selectedHomeScreen
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
                            ImageCard(
                                setBitmap = { bitmap ->
                                    selectedHomeScreen = bitmap
                                    saveImage(context, bitmap, "schedule/$id", "selectedHome")
                                },
                                home = true,
                                width = cardWidth,
                                loadedImage = selectedHomeScreen
                            )
                            // selected lock
                            ImageCard(
                                setBitmap = { bitmap ->
                                    selectedLockScreen = bitmap
                                    saveImage(context, bitmap, "schedule/$id", "selectedLock")
                                },
                                width = cardWidth,
                                loadedImage = selectedLockScreen
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
                                    set = { toggle ->
                                        showLockScreen = toggle

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
                                            scope.launch {
                                                schedulevm.setStartTime(schedule._id, startTime)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                        TimeField("End", endTime12H) { temp, time ->
                                            endTime = temp
                                            endTime12H = time
                                            scope.launch {
                                                schedulevm.setEndTime(schedule._id, endTime)
                                            }
                                        }
                                    }
                                    Column{
                                        Row(
                                            modifier = Modifier
                                                .padding(vertical = 10.dp)

                                        ) {
                                            IconButton(
                                                onClick = {
                                                    scope.launch {
                                                        wallpaperScheduler.scheduleAlarm(
                                                            startTimeString = startTime,
                                                            endTimeString = endTime
                                                        )
                                                        savePref(
                                                            context,
                                                            "schedule_status",
                                                            "scheduled"
                                                        )
                                                        status = "scheduled"
                                                        schedulevm.setRunning(schedule._id, status)
                                                        createNotification(
                                                            context,
                                                            title = "Schedule Set",
                                                            bodyText = "Wallpapers Scheduled\nStart Time: $startTime12H\nEndTime: $endTime12H"
                                                        )
                                                        navController.navigate("Schedule")
                                                    }
                                                },
                                                enabled = (
                                                        (startTime != emptyTime && endTime != emptyTime &&
                                                                (selectedHomeScreen != null && !showLockScreen ||
                                                                        selectedHomeScreen != null && selectedLockScreen != null)
                                                                && (status != "started") && (status != "scheduled"))
                                                        )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.PlayCircle,
                                                    contentDescription = "Start"
                                                )
                                            }
                                            IconButton(
                                                enabled = (status == "started" || status == "scheduled"),
                                                onClick = {
                                                    scope.launch {
//                                                    deleteFolder(context, "schedule/$id")
                                                        withContext(Dispatchers.Main) {
                                                            Toast.makeText(
                                                                context,
                                                                "Cancelling, Reverting wallpapers",
                                                                Toast.LENGTH_SHORT
                                                            )
                                                                .show()
                                                        }
                                                        prevHomeScreen?.let {
                                                            setWallpaper(
                                                                context,
                                                                prevHomeScreen!!,
                                                                0
                                                            )
                                                        }
                                                        if (showLockScreen) {
                                                            prevLockScreen?.let {
                                                                setWallpaper(
                                                                    context,
                                                                    prevLockScreen!!,
                                                                    1
                                                                )
                                                            }
                                                        }
                                                        status = "idle"
                                                        schedulevm.setRunning(schedule._id, status)
                                                        savePref(context, "schedule_status", status)
                                                        navController.navigate("Schedule")
                                                        wallpaperScheduler.cancelAlarm()
                                                        createNotification(
                                                            context,
                                                            title = "Schedule Cancelled",
                                                            bodyText = "Wallpapers Reverted"
                                                        )
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.StopCircle,
                                                    contentDescription = "Start"
                                                )
                                            }
                                            IconButton(

                                                onClick = {
                                                    scope.launch {
                                                        clear()
                                                    }

                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.ClearAll,
                                                    contentDescription = "Start"
                                                )
                                            }
                                            IconButton(

                                                onClick = {
                                                    scope.launch {
                                                        clear()
                                                        schedulevm.deleteSchedule(schedule._id)
                                                        deleteFolder(context, "schedule/$id")
                                                        savePref(context, "schedule_status", "")
                                                        savePref(context, "schedule_id", "")
                                                        navController.navigate("Schedule")
                                                    }

                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.DeleteForever,
                                                    contentDescription = "Start"
                                                )
                                            }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ){
                                            Text("Status: $status")
                                        }
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

