package com.aaditx23.wallpaperwizard.ui.screens

import android.Manifest
import android.annotation.SuppressLint

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.compose.currentBackStackEntryAsState
import com.aaditx23.wallpaperwizard.backend.models.ScheduleModel
import com.aaditx23.wallpaperwizard.backend.viewmodels.QuickSetVM
import com.aaditx23.wallpaperwizard.backend.viewmodels.ScheduleVM
import com.aaditx23.wallpaperwizard.components.BottomNavigation
import com.aaditx23.wallpaperwizard.components.Schedule
import com.aaditx23.wallpaperwizard.components.batteryExemptPermission

import com.aaditx23.wallpaperwizard.components.checkPermission
import com.aaditx23.wallpaperwizard.components.createFolder
import com.aaditx23.wallpaperwizard.components.listFolders
import com.aaditx23.wallpaperwizard.components.models.BottomNavItem.Companion.bottomNavItemList
import com.aaditx23.wallpaperwizard.components.multiPermissionLauncher


import com.aaditx23.wallpaperwizard.components.permissionLauncher
import com.aaditx23.wallpaperwizard.components.requestAllFilesAccess
import com.aaditx23.wallpaperwizard.components.scheduler.WallpaperScheduler
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Main(){
    var selectedIndexBotNav by rememberSaveable {
        mutableIntStateOf(0)
    }

    val qsVM: QuickSetVM = hiltViewModel()
    val schedulevm: ScheduleVM = hiltViewModel()

    val navController = rememberNavController()
    var scope = rememberCoroutineScope()

    var scheduleItem by remember { mutableStateOf<ScheduleModel?>(null) }
    val context = LocalContext.current
    val appContext = LocalContext.current.applicationContext
    val wallpaperScheduler = WallpaperScheduler(appContext)
    val permissions = listOf(
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.SCHEDULE_EXACT_ALARM,
        Manifest.permission.POST_NOTIFICATIONS
    )

    LaunchedEffect(Unit) {
        scope.launch {
            val dir = listFolders(context)
            if(!dir.contains("qs")){
                createFolder(context, "qs")
            }
            if(!dir.contains("schedule")){
                createFolder(context, "schedule")
            }
            println(listFolders(context))

        }
    }


    if(!checkPermission(context, permissions[0])){
        requestAllFilesAccess(context)
    }
    if(!checkPermission(context, permissions[2])){
        batteryExemptPermission(context)
    }
    if(!checkPermission(context, permissions[1]) || !checkPermission(context, permissions[3])){
        multiPermissionLauncher(context, listOf(permissions[1], permissions[3]))
    }


    //-------------------------------



        Scaffold(
            bottomBar = {
                BottomNavigation(
                    selectedIndex = selectedIndexBotNav,
                    onItemSelcted = { index ->
                        selectedIndexBotNav = index
                        navController.navigate(bottomNavItemList[index].title)
                    },
                    fabOnClick = {
                        scope.launch{
                            println("Selected index $selectedIndexBotNav")
                            when (selectedIndexBotNav) {
                                0 -> qsVM.create()
                                2 -> schedulevm.create()
                            }
                        }
                    }
                )
            }
            ) {
            NavHost(navController = navController, startDestination = "QuickSet") {
                // Routes
                composable("QuickSet") {
                    QuickSetScreen(
                        qsVM = qsVM,
                        croppedFolder = {
                            selectedIndexBotNav = -1
                            navController.navigate("Hidden")
                        }
                    )
                }
                composable("Schedule") {
                    ScheduleScreen(schedulevm, navController) { item ->
                        scheduleItem = item
                    }
                }
                composable("Hidden"){
                    PicturesDirectory()
                }
                composable("ScheduleCard"){
                    scheduleItem?.let {
                        wallpaperScheduler.addId(scheduleItem!!._id.toHexString())
                        Schedule(it, schedulevm, wallpaperScheduler, navController)
                    }
                }
            }
        }


}



