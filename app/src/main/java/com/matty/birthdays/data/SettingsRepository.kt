package com.matty.birthdays.data

import android.content.SharedPreferences
import com.matty.birthdays.notifications.DEFAULT_NOTIFICATIONS_HOUR
import com.matty.birthdays.notifications.DEFAULT_NOTIFICATIONS_MINUTE
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton

private const val PREF_NOTIFICATIONS_ENABLED = "NOTIFICATIONS_ENABLED"
private const val PREF_NOTIFICATIONS_HOUR = "NOTIFICATIONS_HOUR"
private const val PREF_NOTIFICATIONS_MINUTE = "NOTIFICATIONS_MINUTE"
private const val DEFAULT_NOTIFICATIONS_ENABLED = true

@Singleton
class SettingsRepository @Inject constructor() {
    @Inject
    lateinit var sharedPrefs: Lazy<SharedPreferences>

    fun getNotificationsSettings() = NotificationsSettings(
        isEnabled = sharedPrefs.get()
            .getBoolean(PREF_NOTIFICATIONS_ENABLED, DEFAULT_NOTIFICATIONS_ENABLED),
        hour = sharedPrefs.get().getInt(PREF_NOTIFICATIONS_HOUR, DEFAULT_NOTIFICATIONS_HOUR),
        minute = sharedPrefs.get().getInt(PREF_NOTIFICATIONS_MINUTE, DEFAULT_NOTIFICATIONS_MINUTE)
    )

    fun saveNotificationsSettings(settings: NotificationsSettings) {
        sharedPrefs.get().edit()
            .putBoolean(PREF_NOTIFICATIONS_ENABLED, settings.isEnabled)
            .putInt(PREF_NOTIFICATIONS_HOUR, settings.hour)
            .putInt(PREF_NOTIFICATIONS_MINUTE, settings.minute)
            .apply()
    }
}