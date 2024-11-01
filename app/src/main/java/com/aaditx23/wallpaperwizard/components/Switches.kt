package com.aaditx23.wallpaperwizard.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aaditx23.wallpaperwizard.ui.theme.buttonThumbTint

//@Composable
//fun LockToggle(set: (Boolean) -> Unit){
//    var check by remember { mutableStateOf(false) }
//    Switch(
//        checked = check,
//        onCheckedChange = {
//            check = it
//            set(it)
//        },
//        thumbContent = {
//            Icon(
//                imageVector = Icons.Filled.Home,
//                contentDescription = "Thumb",
//                modifier = Modifier
//                    .size(15.dp)
//            )
//
//        }
//    )
//}

@Composable
fun LockToggle(set: (Boolean) -> Unit) {
    var check by remember { mutableStateOf(false) }

    Box {
        // Background icon that shows when checked


        // The Switch component
        Switch(
            checked = check,
            onCheckedChange = {
                check = it
                set(it)
            },
            thumbContent = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Thumb",
                    modifier = Modifier.size(15.dp),
                )
            }
        )
        if (check) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Lock Background",
                modifier = Modifier
                    .align(Alignment.CenterStart) // Align to the left inside the switch background
                    .padding(6.dp) // Adjust padding as needed to align within the switch
                    .size(15.dp),
                tint = buttonThumbTint
            )
        }
    }
}