package com.aaditx23.wallpaperwizard.ui.components

import android.annotation.SuppressLint
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
import com.aaditx23.wallpaperwizard.models.ScheduleModel
import com.aaditx23.wallpaperwizard.ui.screens.Schedule.ScheduleVM
import com.aaditx23.wallpaperwizard.ui.components.scheduler.WallpaperScheduler
import kotlinx.coroutines.Dispatchers
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
    var prevHomeScreen by remember { mutableStateOf(schedule.prevHome) }
    var prevLockScreen by remember { mutableStateOf(schedule.prevLock) }
    var scheduledHomeScreen by remember { mutableStateOf(schedule.scheduledHome) }
    var scheduledLockScreen by remember { mutableStateOf(schedule.scheduledLock) }
    val emptyTime = "00:00"
    var startTime by remember { mutableStateOf(schedule.startTime) }
    var endTime by remember { mutableStateOf(schedule.endTime) }
    var showLockScreen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val cardWidth = 100
    val id = schedule._id.toHexString()
    var status by remember { mutableStateOf("idle") }
    val path = schedulevm.croppedDir
    println("PATH IS: $path")
    LaunchedEffect(schedule) {
        scope.launch {
            if(getPref(context, "schedule_id") == id){
                status = getPref(context, "schedule_status")
            }
            if(schedule.prevHome.isEmpty() || (status != "started" && status != "scheduled")){
                getCurrentDrawable(context, 0)?.let {
                    prevHomeScreen = saveImage(context, it.toBitmap(), "prevHome")
                    schedulevm.setPrevHome(schedule._id, prevHomeScreen)
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
                if(schedule.prevLock.isEmpty()){
                    getCurrentDrawable(context, 1)?.let {
                        prevLockScreen = saveImage(context, it.toBitmap(), "prevLock")
                        schedulevm.setPrevLock(schedule._id, prevLockScreen)
                    }
                }
            }
            else{
                if (prevLockScreen.isNotEmpty()) {
                    scope.launch {
                        schedulevm.setPrevLock(schedule._id, "")
                        if(scheduledLockScreen.isNotEmpty()){
                            schedulevm.setScheduledLock(schedule._id, "")
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Deleted Lock Screen for $id",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        prevLockScreen = ""
                        scheduledLockScreen = ""
                    }

                }
            }
            isLoading = false
        }
    }


    fun clear(){
        scope.launch{
            startTime = emptyTime
            endTime = emptyTime
            prevHomeScreen = ""
            prevLockScreen = ""
            scheduledHomeScreen = ""
            scheduledLockScreen = ""
            schedulevm.clearSchedule(schedule._id)
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
                            setImageName = { name ->
                                prevHomeScreen = name
                                schedulevm.setPrevHome(schedule._id, name)
                            },
                            home = true,
                            loadedImageString = "$path/$prevHomeScreen",
                            width = cardWidth,
                            text = "Previous Home",
                            cardColor = MaterialTheme.colorScheme.inversePrimary,
                            iconTint = if (schedule.prevHome.isNotEmpty()) MaterialTheme.colorScheme.inversePrimary
                            else MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        if (showLockScreen) {
                            // previous lock
                            ImageCard(
                                setImageName = {name ->
                                    prevLockScreen = name
                                    schedulevm.setPrevLock(schedule._id, name)
                                },
                                home = false,
                                loadedImageString = "$path/$prevLockScreen",
                                width = cardWidth,
                                text = "Previous Lock",
                                cardColor = MaterialTheme.colorScheme.inversePrimary,
                                iconTint = if (prevLockScreen.isNotEmpty()) MaterialTheme.colorScheme.inversePrimary
                                else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        } else {
                            // selected home
                            ImageCard(
                                setImageName = {name ->
                                    println("NAME IS: $name $path")
                                    scheduledHomeScreen = name
                                    schedulevm.setScheduledHome(schedule._id, name)
                                },
                                home = true,
                                width = cardWidth,
                                loadedImageString = "$path/$scheduledHomeScreen",
                                text = "Scheduled Home"
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
                                setImageName = {name ->
                                    scheduledHomeScreen = name
                                    schedulevm.setScheduledHome(schedule._id, name)
                                },
                                home = true,
                                width = cardWidth,
                                loadedImageString = "$path/$scheduledHomeScreen",
                                text = "Scheduled Home"
                            )
                            // selected lock
                            ImageCard(
                                setImageName = {name ->
                                    scheduledLockScreen = name
                                    schedulevm.setScheduledLock(schedule._id, name)
                                },
                                width = cardWidth,
                                loadedImageString = "$path/$scheduledHomeScreen",
                                text = "Scheduled Lock"
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
                                        TimeField("Start", to12HourString(startTime)) {time ->
                                            startTime = time
                                            schedulevm.setStartTime(schedule._id, time)

                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                        TimeField("End", to12HourString(endTime)) { time ->
                                            endTime = time
                                            schedulevm.setEndTime(schedule._id, time)

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
                                                            bodyText = "Wallpapers Scheduled\nStart Time: ${to12HourString(startTime)}\nEndTime: ${to12HourString(endTime)}"
                                                        )
                                                        navController.navigate("Schedule")
                                                    }
                                                },
                                                enabled = (
                                                        (startTime != emptyTime && endTime != emptyTime &&
                                                                (scheduledHomeScreen.isNotEmpty() && !showLockScreen ||
                                                                        scheduledHomeScreen.isNotEmpty() && scheduledLockScreen.isNotEmpty())
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
                                                        setWallpaper(context, 0, prevHomeScreen)
                                                        if (showLockScreen) {
                                                            setWallpaper(context, 1, prevLockScreen,)
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

