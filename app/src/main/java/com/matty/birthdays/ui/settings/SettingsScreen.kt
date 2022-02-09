package com.matty.birthdays.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matty.birthdays.R
import com.matty.birthdays.ui.component.BirthdaysTopBar
import com.matty.birthdays.ui.component.form.TimeSelector
import com.matty.birthdays.ui.theme.BirthdaysTheme

@Composable
fun SettingsScreen(
    state: NotificationsSettingsState,
    updateNotificationsSettings: (Boolean, Int, Int) -> Unit,
    onBackClicked: () -> Unit
) {
    Scaffold(
        topBar = { TopBar(onBackClicked = { onBackClicked() }) }
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Notifications(
                state = state,
                updateSettings = updateNotificationsSettings
            )
        }
    }
}

@Composable
private fun Notifications(
    state: NotificationsSettingsState,
    updateSettings: (Boolean, Int, Int) -> Unit
) {
    Text(
        text = stringResource(R.string.notifications),
        style = MaterialTheme.typography.h6
    )

    Spacer(modifier = Modifier.height(12.dp))
    Row {
        Column {
            if (state.isEnabled) Text(text = stringResource(R.string.notifications_on))
            else Text(text = stringResource(R.string.notifications_off))
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Switch(
                checked = state.isEnabled,
                onCheckedChange = { isChecked ->
                    updateSettings(isChecked, state.hour, state.minute)
                }
            )
        }
    }

    if (state.isEnabled) {
        Spacer(modifier = Modifier.height(18.dp))
        Row {
            Column {
                Text(text = stringResource(R.string.time))
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimeSelector(
                    hour = state.hour,
                    minute = state.minute,
                    onTimeSelected = { hour, minute ->
                        updateSettings(state.isEnabled, hour, minute)
                    }
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    onBackClicked: () -> Unit
) {
    BirthdaysTopBar {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = stringResource(R.string.back),
            modifier = Modifier.clickable {
                onBackClicked()
            }
        )
        Spacer(modifier = Modifier.width(32.dp))
        Text(
            text = stringResource(R.string.settings),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.h6
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    BirthdaysTheme {
        SettingsScreen(
            state = NotificationsSettingsState(
                isEnabled = true,
                hour = 11,
                minute = 0
            ),
            updateNotificationsSettings = { _, _, _ -> },
            onBackClicked = {}
        )
    }
}