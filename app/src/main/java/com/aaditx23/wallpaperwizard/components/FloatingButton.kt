package com.aaditx23.wallpaperwizard.components

import androidx.compose.foundation.gestures.detectDragGestures

import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable

import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.input.pointer.pointerInput

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

import kotlin.math.roundToInt

@Composable
fun MovableFloatingActionButton(onClick: () -> Unit) {
    // Remember the offset to allow dragging
    val offsetX = rememberSaveable { mutableStateOf(0f) }
    val offsetY = rememberSaveable { mutableStateOf(0f) }

    // Box to hold the button
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 10.dp) // To add some padding from the screen edges
    ) {
        // Small Floating Action Button with drag functionality
        FloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .align(Alignment.BottomEnd) // Default bottom-right position
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        // Update offset values based on the drag amount
                        change.consume() // Consume the drag event to prevent further propagation
                        offsetX.value += dragAmount.x
                        offsetY.value += dragAmount.y
                    }
                },
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Small floating action button.")
        }
    }
}