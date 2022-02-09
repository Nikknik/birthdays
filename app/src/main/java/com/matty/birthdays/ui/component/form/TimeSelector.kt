package com.matty.birthdays.ui.component.form

import android.widget.NumberPicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.matty.birthdays.R

@Composable
fun TimeSelector(
    hour: Int,
    minute: Int,
    modifier: Modifier = Modifier,
    onTimeSelected: (Int, Int) -> Unit
) {
    var isTimeSelectionDialogOpen by remember {
        mutableStateOf(false)
    }

    Text(
        text = "${getFormattedTimeUnit(hour)}:${getFormattedTimeUnit(minute)}",
        modifier = modifier.clickable(
            onClick = { isTimeSelectionDialogOpen = true }
        )
    )

    if (isTimeSelectionDialogOpen) {
        TimeSelectionDialog(
            hour = hour,
            minute = minute,
            onTimeSelected = { selectedHour, selectedMinute ->
                isTimeSelectionDialogOpen = false
                onTimeSelected(selectedHour, selectedMinute)
            },
            onDismiss = { isTimeSelectionDialogOpen = false }
        )
    }
}

@Composable
fun TimeSelectionDialog(
    hour: Int,
    minute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedHour = remember { hour }
    var selectedMinute = remember { minute }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        text = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                AndroidView(factory = { context ->
                    NumberPicker(context).apply {
                        minValue = 0
                        maxValue = 23
                        setFormatter(TimePickerFormatter)
                        setOnScrollListener { numberPicker, _ ->
                            selectedHour = numberPicker.value
                        }
                    }
                },
                    update = {
                        it.value = hour
                    })
                Spacer(
                    modifier = Modifier.width(4.dp)
                )
                AndroidView(factory = { context ->
                    NumberPicker(context).apply {
                        minValue = 0
                        maxValue = 59
                        setFormatter(TimePickerFormatter)
                        setOnScrollListener { numberPicker, _ ->
                            selectedMinute = numberPicker.value
                        }
                    }
                },
                    update = {
                        it.value = minute
                    })
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { onDismiss() }
                ) {
                    Text(text = stringResource(R.string.cancel_btn).uppercase())
                }

                TextButton(
                    onClick = { onTimeSelected(selectedHour, selectedMinute) }
                ) {
                    Text(text = stringResource(R.string.set_btn).uppercase())
                }
            }
        })
}

private object TimePickerFormatter : NumberPicker.Formatter {
    override fun format(value: Int): String {
        return getFormattedTimeUnit(value)
    }
}

private fun getFormattedTimeUnit(value: Int): String {
    val valueString = value.toString()
    if (valueString.length == 2) return valueString
    return "0$valueString"
}