package com.aaditx23.wallpaperwizard.ui.screens

import android.Manifest
import android.annotation.SuppressLint

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.compose.currentBackStackEntryAsState
import com.aaditx23.wallpaperwizard.backend.viewmodels.QuickSetVM
import com.aaditx23.wallpaperwizard.backend.viewmodels.ScheduleVM
import com.aaditx23.wallpaperwizard.components.BottomNavigation

import com.aaditx23.wallpaperwizard.components.checkPermission
import com.aaditx23.wallpaperwizard.components.createFolder
import com.aaditx23.wallpaperwizard.components.deleteFolder
import com.aaditx23.wallpaperwizard.components.listFolders
import com.aaditx23.wallpaperwizard.components.models.BottomNavItem.Companion.bottomNavItemList



import com.aaditx23.wallpaperwizard.components.permissionLauncher
import com.aaditx23.wallpaperwizard.components.requestAllFilesAccess
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
    var scrollState = rememberScrollState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val context = LocalContext.current
    val permissions = listOf(
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.READ_MEDIA_IMAGES
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



//    LaunchedEffect(navBackStackEntry?.destination) {
//        when (navBackStackEntry?.destination?.route) {
//            "Profile" -> selectedIndexDrawer = navDrawerItems.indexOfFirst { it.title == "Profile" }
//            "About BUCC" -> selectedIndexDrawer = navDrawerItems.indexOfFirst { it.title == "About BUCC" }
//            "About Us" -> selectedIndexDrawer = navDrawerItems.indexOfFirst { it.title == "About Us" }
//            "Login" -> selectedIndexDrawer = navDrawerItems.indexOfFirst { it.title == "Login" }
//            "SE Dashboard" -> selectedIndexDrawer = navDrawerItems.indexOfFirst { it.title == "SE Dashboard" }
//            // Add other routes here if needed
//        }
//    }

    if(!checkPermission(context, permissions[0])){
        requestAllFilesAccess(context)
    }
    if(!checkPermission(context, permissions[1])){
        permissionLauncher(context, Manifest.permission.READ_MEDIA_IMAGES)
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
                        if(selectedIndexBotNav == 0){
                            qsVM.createQuickSet()
                        }
                    }
                )
            }
            ) {
            NavHost(navController = navController, startDestination = "Quick Set") {
                // Routes
                composable("Quick Set") {
                    QuickSetScreen(
                        qsVM = qsVM,
                        croppedFolder = {
                            selectedIndexBotNav = -1
                            navController.navigate("Hidden")
                        }
                    )
                }
                composable("Schedule") {
                    Schedule()
                }
                composable("Hidden"){
                    PicturesDirectory()
                }

            }
        }


}



