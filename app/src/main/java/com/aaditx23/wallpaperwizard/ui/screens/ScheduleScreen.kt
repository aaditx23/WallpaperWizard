package com.aaditx23.wallpaperwizard.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aaditx23.wallpaperwizard.backend.models.ScheduleModel
import com.aaditx23.wallpaperwizard.backend.viewmodels.ScheduleVM
import com.aaditx23.wallpaperwizard.components.CircularLoadingBasic
import com.aaditx23.wallpaperwizard.components.Schedule
import com.aaditx23.wallpaperwizard.components.getPref
import com.aaditx23.wallpaperwizard.components.scheduler.WallpaperScheduler
import com.aaditx23.wallpaperwizard.components.to12HourString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ScheduleScreen(
    schedulevm: ScheduleVM,
    navController: NavHostController,
    passSchedule: (ScheduleModel) -> Unit
) {

    val allSchedules by schedulevm.allSchedules.collectAsState()
    val isLoading by schedulevm.isLoading.collectAsState()
    var click by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    if(isLoading){
        CircularLoadingBasic("Please wait...")
    }
    else{
        Column(
            modifier = Modifier
                .padding(top = 80.dp)
        ){
//            Button(
//                onClick = croppedFolder,
//                modifier = Modifier
//                    .fillMaxWidth(),
//                shape = RectangleShape
//            ) {
//                Text("Cropped Pictures Cache")
//            }
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 110.dp)
            ) {
                itemsIndexed(allSchedules) { index, scheduleItem ->
                    var status by remember { mutableStateOf("") }
                    var prefId by remember { mutableStateOf("") }
                    var loading by remember { mutableStateOf(true) }
                    scope.launch {
                        prefId = getPref(context, "schedule_id")
                        status = if(prefId == scheduleItem._id.toHexString()){
                            getPref(context, "schedule_status")
                        } else {
                            "idle"
                        }
                        delay(100)
                        schedulevm.setRunning(scheduleItem._id, status)
                        loading = false
                    }
                    if(loading){
                        CircularLoadingBasic("Loading...")
                    }
                    else{
                        ElevatedCard(
                            onClick = {
                                passSchedule(scheduleItem)
                                navController.navigate("ScheduleCard")
                            },
                            modifier = Modifier
                                .padding(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
//                                Text(index.toString())
                                Text("Start: ${
                                    if(scheduleItem.startTime == null) "Not set"
                                    else to12HourString(scheduleItem.startTime!!)
                                }")
                                Text("End: ${
                                    if(scheduleItem.startTime == null) "Not set"
                                    else to12HourString(scheduleItem.endTime!!)
                                }")
                                Text("Status: $status")
                            }
                        }
                    }

                }
            }
        }
    }
}
