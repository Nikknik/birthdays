package com.matty.birthdays.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.matty.birthdays.NOTIFICATION_CHANNEL_ID
import com.matty.birthdays.R
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.data.BirthdayRepository
import com.matty.birthdays.data.ContactsSynchronizer
import com.matty.birthdays.ui.MainActivity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.random.Random

//24days*60minutes*60seconds*1000milliseconds
private const val NOTIFICATION_INTERVAL = 24*60*60*1000L
//11 a.m.
private const val NOTIFICATION_TIME = 11
private const val TAG = "NotificationReceiver"

class NotificationReceiver: BroadcastReceiver() {

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface NotificationReceiverEntryPoint {
        fun repository(): BirthdayRepository
        fun contactsSynchronizer(): ContactsSynchronizer
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: started")

        GlobalScope.launch(Dispatchers.IO) {
            val requireContext = context!!
            val synchronizer = getContactsSynchronizer(requireContext)
            synchronizer.synchronize()

            val birthdays = getRepository(requireContext).getBirthdaysToday()
            Log.d(TAG, "birthdays today: ${birthdays.joinToString { it.name }}")
            if(birthdays.isNotEmpty()) {
                notify(requireContext, birthdays)
            }
        }
    }

    private fun notify(context: Context, birthdays: List<Birthday>) {
        val intent = MainActivity.createIntent(context)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_cake)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(birthdays.joinToString { it.name })
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(Random.nextInt(), builder.build())
        }

        Log.d(TAG, "notification sent")
    }

    private fun getRepository(context: Context): BirthdayRepository {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            NotificationReceiverEntryPoint::class.java
        )
        return entryPoint.repository()
    }

    private fun getContactsSynchronizer(context: Context): ContactsSynchronizer {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            NotificationReceiverEntryPoint::class.java
        )
        return entryPoint.contactsSynchronizer()
    }

    companion object {
        fun schedule(context: Context) {
            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val firstRunTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, NOTIFICATION_TIME)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                firstRunTime.timeInMillis,
                NOTIFICATION_INTERVAL,
                pendingIntent
            )
        }
    }
}