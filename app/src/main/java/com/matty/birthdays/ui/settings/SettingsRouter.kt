package com.matty.birthdays.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.matty.birthdays.navigation.NavAdapter

@Composable
fun SettingsRouter(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navAdapter: NavAdapter
) {
    SettingsScreen(
        state = settingsViewModel.settingsFlow.collectAsState().value,
        updateNotificationsSettings = settingsViewModel::updateNotificationsState,
        onBackClicked = {
            settingsViewModel.updateSettings()
            navAdapter.goBack()
        }
    )
}