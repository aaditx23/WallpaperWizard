package com.aaditx23.wallpaperwizard.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WatchLater

import androidx.compose.material3.AlertDialog

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(timePickerState) }) {
                Text("OK")
            }
        },
        text = {
            TimePicker(
                state = timePickerState,
            )
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeField(
    label: String,
    text: String,
    setTime: (String, String) -> Unit

){
    var showTimePicker by remember { mutableStateOf(false) }
    var time by remember { mutableStateOf("00:00") }
    OutlinedTextField(
        value = text,
        onValueChange = {

        },
        readOnly = true,
        label = {
            Text(label)
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.WatchLater,
                contentDescription = label,
                modifier = Modifier
                    .clickable {
                        showTimePicker = true
                    }
                    .size(20.dp)
            )
        }
    )

    if(showTimePicker){
        TimePicker(
            onConfirm = { timePicker ->
                val temp = "${timePicker.hour}:${timePicker.minute}"
                time = to12Hour(timePicker)
                setTime(temp, time)
                showTimePicker = false
            },
            onDismiss = {
                showTimePicker = false
            }
        )
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
fun to12Hour(timePickerState: TimePickerState): String {
    val hour = timePickerState.hour
    val minute = timePickerState.minute

    val period = if (hour >= 12) "PM" else "AM"
    val formattedHour = if (hour % 12 == 0) 12 else hour % 12  // Converts hour to 12-hour format

    return String.format("%02d : %02d %s", formattedHour, minute, period)
}

@SuppressLint("DefaultLocale")
fun to12HourString(time: String): String {
    val temp = time.split(":")
    val hour = temp[0].trim().toInt()
    val minute = temp[1].trim().toInt()

    val period = if (hour >= 12) "PM" else "AM"
    val formattedHour = if (hour % 12 == 0) 12 else hour % 12  // Converts hour to 12-hour format

    return String.format("%02d : %02d %s", formattedHour, minute, period)
}

