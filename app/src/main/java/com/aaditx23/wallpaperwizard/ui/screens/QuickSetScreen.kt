package com.aaditx23.wallpaperwizard.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aaditx23.wallpaperwizard.backend.viewmodels.QuickSetVM
import com.aaditx23.wallpaperwizard.components.CircularLoadingBasic

@Composable
fun QuickSetScreen(qsVM: QuickSetVM) {

    val allQuickSets by qsVM.allQuickSets.collectAsState()
    val isLoading by qsVM.isLoading.collectAsState()

    if(isLoading){
        CircularLoadingBasic("Please wait...")
    }
    else{
        LazyColumn(
            modifier = Modifier
                .padding(top = 70.dp, bottom = 110.dp)
        ) {
            items(allQuickSets){ quickSetItem ->
                QuickSetCard(qsVM, quickSetItem)
            }
        }
    }
}