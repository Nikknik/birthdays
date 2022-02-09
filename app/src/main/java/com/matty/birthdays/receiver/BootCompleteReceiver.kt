package com.matty.birthdays.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.matty.birthdays.data.SettingsRepository
import com.matty.birthdays.notifications.NotificationsScheduler
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

private const val TAG = "BootCompleteReceiver"

class BootCompleteReceiver : BroadcastReceiver() {

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface BootCompleteReceiverEntryPoint {
        fun settingsRepository(): SettingsRepository
        fun scheduler(): NotificationsScheduler
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: started")
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val notificationSettings = getRepository(context!!).getNotificationsSettings()
            Log.d(
                TAG, "notifications: " +
                        "enabled: ${notificationSettings.isEnabled} " +
                        "time: ${notificationSettings.hour}:${notificationSettings.minute}"
            )

            if (notificationSettings.isEnabled) {
                getNotificationsScheduler(context).schedule(
                    hour = notificationSettings.hour,
                    minute = notificationSettings.minute
                )
            }
            Log.d(TAG, "notifications scheduled")
        }
    }

    private fun getRepository(context: Context): SettingsRepository {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            BootCompleteReceiverEntryPoint::class.java
        )
        return entryPoint.settingsRepository()
    }

    private fun getNotificationsScheduler(context: Context): NotificationsScheduler {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            BootCompleteReceiverEntryPoint::class.java
        )
        return entryPoint.scheduler()
    }
}