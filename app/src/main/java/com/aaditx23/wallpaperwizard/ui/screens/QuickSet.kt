package com.aaditx23.wallpaperwizard.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.wallpaperwizard.components.CircularLoadingBasic
import com.aaditx23.wallpaperwizard.components.LockToggle
import com.aaditx23.wallpaperwizard.components.SelectedWallpaper
import com.aaditx23.wallpaperwizard.components.setWallpaper
import com.aaditx23.wallpaperwizard.ui.theme.palette6LightIndigo
import com.aaditx23.wallpaperwizard.ui.theme.palette6LightOrchid
import com.aaditx23.wallpaperwizard.ui.theme.palette6LightSlateBlue1
import com.aaditx23.wallpaperwizard.ui.theme.palette6MagicMint
import com.aaditx23.wallpaperwizard.ui.theme.palette6PalePink
import com.aaditx23.wallpaperwizard.ui.theme.palette6PowderBlue
import com.aaditx23.wallpaperwizard.ui.theme.palette7Green1
import com.aaditx23.wallpaperwizard.ui.theme.paletteBlue2
import com.aaditx23.wallpaperwizard.ui.theme.paletteBlue7
import com.aaditx23.wallpaperwizard.ui.theme.paletteBlue9
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun QuickSetScreen(){
    var selectedHomeScreen by remember { mutableStateOf<Bitmap?>(null) }
    var selectedLockScreen by remember { mutableStateOf<Bitmap?>(null) }
    var showLockScreen by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ElevatedCard(
        onClick = {},
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(palette6LightIndigo)
    ) {
        Column(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                LockToggle { toggle ->
                    showLockScreen = toggle
                }
            }
            Row {
                SelectedWallpaper(
                    setBitmap = { image ->
                        selectedHomeScreen = image
                    },
                    text = "Selected Home"
                )
                if (showLockScreen) {
                    SelectedWallpaper(
                        setBitmap = { image ->
                            selectedLockScreen = image
                        },
                        text = "Selected Lock"
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(170.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    CardButton(
                        text = "Set Wallpaper(s)",
                        buttonColor = MaterialTheme.colorScheme.primary
                    ) {
                        scope.launch {
                            if(selectedHomeScreen != null){
                                val result = async{
                                    setWallpaper(
                                        context = context,
                                        bitmap = selectedHomeScreen!!,
                                        index = 0
                                    )
                                }.await()
                                withContext(Dispatchers.Main){
                                    if (result){
                                        Toast.makeText(context, "Wallpapers set successfully", Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        Toast.makeText(context, "There was an error setting wallpapers", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    CardButton(
                        text = "Delete QuickSet",
                        buttonColor = MaterialTheme.colorScheme.error
                    ) {

                    }

                }
            }
        }
    }
}

@Composable
fun CardButton(
    text: String,
    buttonColor: Color,
    textColor: Color = Color.White,
    onClick: () -> Unit
){
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .width(150.dp)
            .height(50.dp),
        colors = CardDefaults.cardColors(buttonColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = text,
                color = textColor,
                fontSize = 15.sp
            )
        }
    }
}