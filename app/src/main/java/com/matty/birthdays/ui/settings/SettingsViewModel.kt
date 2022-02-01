package com.matty.birthdays.ui.settings

import androidx.lifecycle.ViewModel
import com.matty.birthdays.data.NotificationsSettings
import com.matty.birthdays.data.SettingsRepository
import com.matty.birthdays.notifications.NotificationsScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class NotificationsSettingsState(
    val isEnabled: Boolean,
    val hour: Int,
    val minute: Int
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
    private val notificationsScheduler: NotificationsScheduler
) : ViewModel() {

    private val _settingsFlow: MutableStateFlow<NotificationsSettingsState> = MutableStateFlow(
        repository.getNotificationsSettings().toState()
    )

    val settingsFlow = _settingsFlow.asStateFlow()

    fun updateNotificationsState(isEnabled: Boolean, hour: Int, minute: Int) {
        _settingsFlow.value = NotificationsSettingsState(isEnabled, hour, minute)
    }

    fun updateSettings() {
        val settings = settingsFlow.value.toSettings()
        repository.saveNotificationsSettings(settings)
        if (settings.isEnabled) {
            notificationsScheduler.schedule(
                hour = settings.hour,
                minute = settings.minute
            )
        } else {
            notificationsScheduler.stop()
        }
    }
}

fun NotificationsSettings.toState() =
    NotificationsSettingsState(
        isEnabled = this.isEnabled,
        hour = this.hour,
        minute = this.minute
    )

fun NotificationsSettingsState.toSettings() =
    NotificationsSettings(
        isEnabled = this.isEnabled,
        hour = this.hour,
        minute = this.minute
    )