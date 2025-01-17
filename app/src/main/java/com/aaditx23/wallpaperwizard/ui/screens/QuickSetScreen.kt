package com.aaditx23.wallpaperwizard.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.aaditx23.wallpaperwizard.backend.viewmodels.QuickSetVM
import com.aaditx23.wallpaperwizard.components.CircularLoadingBasic
import com.aaditx23.wallpaperwizard.components.EmptyScreenText
import com.aaditx23.wallpaperwizard.components.QuickSetCard

@Composable
fun QuickSetScreen(
    qsVM: QuickSetVM,
    croppedFolder: () -> Unit
) {

    val allQuickSets by qsVM.allQuickSets.collectAsState()
    val isLoading by qsVM.isLoading.collectAsState()

    if(isLoading){
        CircularLoadingBasic("Please wait...")
    }
    else{
        Column(
            modifier = Modifier
                .padding(top = 60.dp)
        ){
            Box(
              modifier = Modifier
                  .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ){
                IconButton(
                    onClick = croppedFolder,
                ) {
                    Icon(
                        imageVector = Icons.Filled.DeleteSweep,
                        contentDescription = "Cche"
                    )
                }
            }
            if(allQuickSets.isEmpty()){
                EmptyScreenText("No QuickSets")
            }
            else{
                LazyColumn(
                    modifier = Modifier
                        .padding(bottom = 110.dp)
                ) {
                    items(allQuickSets) { quickSetItem ->
                        QuickSetCard(qsVM, quickSetItem)
                    }
                }
            }
        }
    }
}