package com.aaditx23.wallpaperwizard.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aaditx23.wallpaperwizard.components.MovableFloatingActionButton

@Composable
fun QuickSetScreen(){

    MovableFloatingActionButton {

    }
    LazyColumn(
        modifier = Modifier
            .padding(top = 70.dp, bottom = 110.dp)
    ) {
        item{
            QuickSetCard()
            QuickSetCard()
            QuickSetCard()
            QuickSetCard()
            QuickSetCard()
        }
    }
}