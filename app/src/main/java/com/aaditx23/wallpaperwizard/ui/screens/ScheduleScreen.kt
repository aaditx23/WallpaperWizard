package com.aaditx23.wallpaperwizard.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aaditx23.wallpaperwizard.backend.models.ScheduleModel
import com.aaditx23.wallpaperwizard.backend.viewmodels.ScheduleVM
import com.aaditx23.wallpaperwizard.components.CircularLoadingBasic
import com.aaditx23.wallpaperwizard.components.Schedule
import com.aaditx23.wallpaperwizard.components.scheduler.WallpaperScheduler

@Composable
fun ScheduleScreen(
    schedulevm: ScheduleVM,
    navController: NavHostController,
    passSchedule: (ScheduleModel) -> Unit
) {

    val allSchedules by schedulevm.allSchedules.collectAsState()
    val isLoading by schedulevm.isLoading.collectAsState()
    var click by remember { mutableStateOf(false) }


    if(isLoading){
        CircularLoadingBasic("Please wait...")
    }
    else{
        println(allSchedules.size)
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
                items(allSchedules) { scheduleItem ->
                    ElevatedCard(
                        onClick = {
                            passSchedule(scheduleItem)
                            navController.navigate("ScheduleCard")
                        }
                    ) {
                        Text(scheduleItem._id.toHexString())
                    }

                }
            }
        }
    }
}
