package com.aaditx23.wallpaperwizard.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aaditx23.wallpaperwizard.models.QuickSetModel
import com.aaditx23.wallpaperwizard.ui.screens.QuickSet.QuickSetVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun QuickSetCard(qsVM: QuickSetVM, quickSetItem: QuickSetModel) {

    var selectedHomeString by remember { mutableStateOf<String>(quickSetItem.home) }
    var selectedLockString by remember { mutableStateOf<String>(quickSetItem.lock) }

    var showLockScreen by remember { mutableStateOf(false) }
    var setHomeScreen by remember { mutableStateOf<Boolean?>(null) }
    var setLockScreen by remember { mutableStateOf<Boolean?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val id = quickSetItem._id.toHexString()
    val path = qsVM.croppedDir

    LaunchedEffect(quickSetItem) {

        showLockScreen = quickSetItem.lock != ""


    }

    ElevatedCard(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                LockToggle(
                    hasLock = showLockScreen,
                    set = { toggle ->
                        showLockScreen = toggle
                        if (!toggle && selectedLockString.isNotEmpty()) {
                            scope.launch {
                                qsVM.removeLockScreen(quickSetItem._id)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Deleted Lock Screen for $id",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        }
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ImageCard(
                    setImageName = { name ->
                        selectedHomeString = name
                        qsVM.addHomeScreen(quickSetItem._id, name)
                    },
                    home = true,
                    id = id,
                    loadedImageString = "$path/$selectedHomeString"
                )
                if (showLockScreen) {
                    ImageCard(
                        setImageName = { name ->
                            selectedLockString = name
                            qsVM.addLockScreen(quickSetItem._id, name)
                        },
                        id = id,
                        loadedImageString = "$path/$selectedLockString"
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(200.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                if (selectedHomeString.isNotEmpty()) {
                                    setHomeScreen = async {
                                        setWallpaper(
                                            context = context,
                                            name = "$path/$selectedHomeString",
                                            index = 0
                                        )
                                    }.await()
                                }
                                if(selectedLockString.isNotEmpty()){
                                    setLockScreen = async {
                                        setWallpaper(
                                            context = context,
                                            name = "$path/$selectedLockString",
                                            index = 1
                                        )
                                    }.await()
                                }
                                isLoading = false

                                if (setHomeScreen != null) {
                                    if(setHomeScreen!!){
                                        withContext(Dispatchers.Main){
                                            Toast.makeText(
                                                context,
                                                "Home Screen Wallpaper set successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    else {
                                        withContext(Dispatchers.Main){
                                            Toast.makeText(
                                                context,
                                                "There was an error setting Home Screen wallpapers",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    setHomeScreen = null
                                }
                                if (setLockScreen != null) {
                                    if(setLockScreen!!){
                                        withContext(Dispatchers.Main){
                                            Toast.makeText(
                                                context,
                                                "Lock Screen Wallpaper set successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    else {
                                        withContext(Dispatchers.Main){
                                            Toast.makeText(
                                                context,
                                                "There was an error setting Lock screen wallpaper",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    setLockScreen = null
                                }

                            }
                        }
                    ) {
                        if(isLoading){
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                        else{
                            Icon(
                                imageVector = Icons.Filled.PlayCircle,
                                contentDescription = "Set Wallpaper",
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    IconButton(
                        onClick = {
                            scope.launch {
                                qsVM.deleteQuickSet(quickSetItem._id)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            contentDescription = "Delete QuickSet",
                            modifier = Modifier
                                .size(50.dp)
                        )
                    }
                }
            }
        }
    }
}
