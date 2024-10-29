package com.aaditx23.wallpaperwizard.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aaditx23.wallpaperwizard.components.NavDrawer
import com.aaditx23.wallpaperwizard.components.TopActionBar
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

import androidx.navigation.compose.currentBackStackEntryAsState

import com.aaditx23.wallpaperwizard.components.CircularLoadingBasic
import com.aaditx23.wallpaperwizard.components.models.NavDrawerItem.Companion.navDrawerItems
import com.aaditx23.wallpaperwizard.components.multiplePermissionLauncher
import com.aaditx23.wallpaperwizard.components.permissionLauncher
import com.aaditx23.wallpaperwizard.components.requestAllFilesAccess
import kotlinx.coroutines.async
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Main(){
    var selectedIndexBotNav by rememberSaveable {
        mutableIntStateOf(0)
    }
    var selectedIndexDrawer by rememberSaveable {
        mutableIntStateOf(-1)
    }

    val navController = rememberNavController()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var scope = rememberCoroutineScope()
    var scrollState = rememberScrollState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val context = LocalContext.current



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

    requestAllFilesAccess(context)
//    multiplePermissionLauncher(context, listOf(
//        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
//
//    ))

    //-------------------------------

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavDrawer(
                    scrollState = scrollState,
                    selectedIndex = selectedIndexDrawer,
                    onClick = { item ->
                        selectedIndexDrawer = navDrawerItems.indexOf(item)
                        navController.navigate(item.title)
                        selectedIndexBotNav = -1
                        scope.launch {
                            drawerState.close()
                        }
                    },

                    )
            }
        },
        gesturesEnabled = true
    ) {

        Scaffold(
//            bottomBar = {
//                BottomNavigation(selectedIndex = selectedIndexBotNav) { index ->
//                    selectedIndexBotNav = index
//                    selectedIndexDrawer = -1
//                    println("$selectedIndexDrawer $selectedIndexBotNav")
//                    when (index) {
//                        0 -> navController.navigate("Profile")
//                        1 -> navController.navigate("Dashboard")
//                    }
//
//                }
//            },
            topBar = {
                TopActionBar(drawerState = drawerState, scope = scope)
            },

            ) {
            NavHost(navController = navController, startDestination = "Quick Set") {
                // Routes
                composable("Quick Set") {
                    CurrentWallpaperImage()
                }

            }
        }

    }
//    if (isSessionReady){
//
//    }
//    else{
//        CircularLoadingBasic("Please wait...")
//    }
}


