package com.matty.birthdays.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.matty.birthdays.receiver.NotificationReceiver
import java.util.Calendar
import javax.inject.Inject

const val DEFAULT_NOTIFICATIONS_HOUR = 11
const val DEFAULT_NOTIFICATIONS_MINUTE = 0

//24hours*60minutes*60seconds*1000milliseconds = 1 day
private const val NOTIFICATION_INTERVAL = 24 * 60 * 60 * 1000L

class NotificationsScheduler @Inject constructor(context: Context) {
    private val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context, NotificationReceiver::class.java),
        PendingIntent.FLAG_CANCEL_CURRENT
    )

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(
        hour: Int = DEFAULT_NOTIFICATIONS_HOUR,
        minute: Int = DEFAULT_NOTIFICATIONS_MINUTE
    ) {
        val firstRunTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            firstRunTime.timeInMillis,
            NOTIFICATION_INTERVAL,
            pendingIntent
        )
    }

    fun stop() {
        alarmManager.cancel(pendingIntent)
    }
}