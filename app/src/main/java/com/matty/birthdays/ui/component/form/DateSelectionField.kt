package com.matty.birthdays.ui.component.form

import android.util.Log
import android.widget.NumberPicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Event
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.matty.birthdays.R
import com.matty.birthdays.data.DateOfBirth
import com.matty.birthdays.utils.format
import java.text.DateFormatSymbols
import java.util.Calendar

private const val TAG = "DateSelectionField"
private const val LEAP_YEAR = 2016

@Composable
fun DateSelectionField(
    date: DateOfBirth?,
    onSelect: (DateOfBirth) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enabled: Boolean = true
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val formattedDate = remember(date) {
        date?.format() ?: ""
    }
    val focusManager = LocalFocusManager.current
    val isPressed = interactionSource.collectIsPressedAsState().value
    if (enabled && isPressed) {
        showDialog = true
    }
    Log.d(TAG, "DateSelectionField: $date")

    OutlinedTextField(
        value = formattedDate,
        readOnly = true,
        enabled = enabled,
        modifier = modifier,
        onValueChange = {},
        interactionSource = interactionSource,
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Event, contentDescription = "")
        },
        trailingIcon = {
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
        },
        placeholder = {
            Text(text = stringResource(R.string.birthday_date_placeholder))
        },
        isError = isError
    )

    if (showDialog) {
        DateSelectionDialog(
            onDateSelected = { dateTriple ->
                showDialog = false
                onSelect(dateTriple)
                focusManager.clearFocus()
            },
            onDismissRequest = {
                showDialog = false
                focusManager.clearFocus()
            },
            date = date ?: DateOfBirth.now()
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DateSelectionDialog(
    date: DateOfBirth,
    onDateSelected: (DateOfBirth) -> Unit,
    onDismissRequest: () -> Unit
) {
    var day by remember {
        mutableStateOf(date.day)
    }
    var month by remember {
        mutableStateOf(date.month)
    }
    var year by remember {
        mutableStateOf(date.year ?: LEAP_YEAR)
    }
    var includeYears by remember {
        mutableStateOf(true)
    }
    val maxDay = remember(includeYears, month, year) {
        Calendar.getInstance().apply {
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, if (includeYears) year else LEAP_YEAR)

        }.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    AlertDialog(
        text = {
            val checkboxInteractionSource = remember {
                MutableInteractionSource()
            }
            Surface {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable(
                            enabled = true,
                            interactionSource = checkboxInteractionSource,
                            indication = LocalIndication.current,
                            onClick = { includeYears = !includeYears }
                        )
                    ) {
                        Checkbox(
                            checked = includeYears,
                            onCheckedChange = { includeYears = !includeYears },
                            interactionSource = checkboxInteractionSource
                        )
                        Text(text = stringResource(R.string.include_year))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        AndroidView(factory = { context ->
                            NumberPicker(context).apply {
                                minValue = 1
                                maxValue = 31 //TODO
                                setOnScrollListener { numberPicker, _ ->
                                    day = numberPicker.value
                                }
                            }
                        }, update = {
                            it.value = day
                            it.maxValue = maxDay
                        })
                        AndroidView(factory = { context ->
                            NumberPicker(context).apply {
                                minValue = 0
                                maxValue = 11
                                displayedValues = DateFormatSymbols().months
                                setOnScrollListener { numberPicker, _ ->
                                    month = numberPicker.value
                                }
                            }
                        }, update = {
                            it.value = month
                        })
                        AnimatedVisibility(visible = includeYears) {
                            AndroidView(factory = { context ->
                                NumberPicker(context).apply {
                                    minValue = 1900
                                    maxValue = 2021 //TODO
                                    setOnScrollListener { numberPicker, _ ->
                                        year = numberPicker.value
                                    }
                                }
                            }, update = {
                                it.value = year
                            })
                        }

                    }
                }
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    onDismissRequest()
                }) {
                    Text(text = stringResource(R.string.cancel_btn).uppercase())
                }
                TextButton(onClick = {
                    onDateSelected(
                        DateOfBirth(
                            day = day,
                            month = month,
                            year = year.takeIf { includeYears }
                        )
                    )
                }) {
                    Text(text = stringResource(R.string.set_btn).uppercase())
                }
            }
        },
        onDismissRequest = onDismissRequest
    )
}