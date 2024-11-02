package com.aaditx23.wallpaperwizard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.wallpaperwizard.ui.theme.paletteBlue1
import com.aaditx23.wallpaperwizard.ui.theme.paletteBlue3
import com.aaditx23.wallpaperwizard.ui.theme.paletteBlue7
import com.aaditx23.wallpaperwizard.ui.theme.paletteBlue9
import kotlinx.coroutines.launch

@Composable
fun DayBar(
    selectedIndex: MutableSet<Int>,
    onClick: (i: Int)-> Unit,
){
    val list = listOf(
        "Su",
        "Mo",
        "Tu",
        "We",
        "Th",
        "Fr"
    )
    val selectedColor = MaterialTheme.colorScheme.inversePrimary
    val unselectedColor = MaterialTheme.colorScheme.onSecondaryContainer
    val selected by rememberSaveable { mutableStateOf(mutableSetOf<Int>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(selectedIndex) {
        println(selectedIndex)
        scope.launch {
            selectedIndex.forEach{
                selected.add(it)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,

        ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            itemsIndexed(list){i, s ->
                val color = if (selected.contains(i)){
                    CardDefaults.cardColors(selectedColor)
                } else {
                    CardDefaults.cardColors(unselectedColor)
                }
                println(selected.contains(i))
                ElevatedCard(
                    onClick = {
                        onClick(i)
                        println("selected index $selected")

                    },
                    colors = color,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(width = 40.dp, height = 35.dp),
                    elevation = CardDefaults.cardElevation(20.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = s.slice(0..1),
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    }
                }
            }
        }
    }
}
