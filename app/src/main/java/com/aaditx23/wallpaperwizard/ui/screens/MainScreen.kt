package com.aaditx23.wallpaperwizard.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
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

import androidx.compose.ui.platform.LocalContext

import androidx.navigation.compose.currentBackStackEntryAsState
import com.aaditx23.wallpaperwizard.components.BottomNavigation
import com.aaditx23.wallpaperwizard.components.MovableFloatingActionButton

import com.aaditx23.wallpaperwizard.components.checkPermission
import com.aaditx23.wallpaperwizard.components.models.BottomNavItem.Companion.bottomNavItemList

import com.aaditx23.wallpaperwizard.components.models.NavDrawerItem.Companion.navDrawerItems

import com.aaditx23.wallpaperwizard.components.permissionLauncher
import com.aaditx23.wallpaperwizard.components.requestAllFilesAccess


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Main(){
    var selectedIndexBotNav by rememberSaveable {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var scope = rememberCoroutineScope()
    var scrollState = rememberScrollState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val context = LocalContext.current
    val permissions = listOf(
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.READ_MEDIA_IMAGES
    )



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
                ) { index ->
                    selectedIndexBotNav = index
                    navController.navigate(bottomNavItemList[index].title)
                }
            },
            topBar = {
                TopActionBar(drawerState = drawerState, scope = scope)
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                MovableFloatingActionButton {

                }
            },


            ) {
            NavHost(navController = navController, startDestination = "Quick Set") {
                // Routes
                composable("Quick Set") {
                    QuickSetScreen()
                }
                composable("Schedule") {
                    Schedule()
                }

            }
        }


}



