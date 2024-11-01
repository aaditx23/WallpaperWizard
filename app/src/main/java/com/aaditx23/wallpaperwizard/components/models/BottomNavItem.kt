package com.aaditx23.wallpaperwizard.components.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.FastForward
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    object QuickSet: BottomNavItem(
        selectedIcon = Icons.Filled.FastForward,
        unselectedIcon = Icons.Outlined.FastForward,
        title = "Quick Set",
    )
    object Schedule: BottomNavItem(
        selectedIcon = Icons.Filled.Timer,
        unselectedIcon = Icons.Outlined.Timer,
        title = "Schedule",
    )
    companion object{
        val bottomNavItemList = listOf(
            QuickSet,
            Schedule
        )

    }



}

