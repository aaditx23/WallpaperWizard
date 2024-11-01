package com.aaditx23.wallpaperwizard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.wallpaperwizard.ui.theme.paletteBlue1
import com.aaditx23.wallpaperwizard.ui.theme.paletteBlue3
import com.aaditx23.wallpaperwizard.ui.theme.paletteBlue7
import com.aaditx23.wallpaperwizard.ui.theme.paletteBlue9

@Composable
fun DayBar(
    list: List<String>,
    selectedIndex: Int,
    onClick: (i: Int)-> Unit,
    topPadding: Dp = 119.dp
){
    val selectedColor = paletteBlue7
    val unselectedColor = paletteBlue3
    val selectedTextColor = paletteBlue1
    val unselectedTextColor = paletteBlue9
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,

        ) {
        Row(
            modifier = Modifier
                .padding(top = topPadding)
//                .background(Color.LightGray)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            println(list)
            list.forEachIndexed { i, s ->
                Card(
                    onClick = {
                        onClick(i)
                    },
                    colors = if (selectedIndex == i){
                        CardDefaults.cardColors(selectedColor)
                    } else {
                        CardDefaults.cardColors(unselectedColor)
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .size(width = 40.dp, height = 35.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = s.slice(0..1),
                            fontSize = 15.sp,
                            color = if(selectedIndex == i) selectedTextColor
                            else unselectedTextColor
                        )
                    }
                }
            }
        }
    }
}
