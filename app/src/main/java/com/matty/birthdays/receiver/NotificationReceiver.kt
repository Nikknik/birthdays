package com.matty.birthdays.receiver

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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val TAG = "NotificationReceiver"

class NotificationReceiver : BroadcastReceiver() {

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface NotificationReceiverEntryPoint {
        fun repository(): BirthdayRepository
        fun contactsSynchronizer(): ContactsSynchronizer
    }

    @DelicateCoroutinesApi
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: started")

        GlobalScope.launch(Dispatchers.IO) {
            val synchronizer = getContactsSynchronizer(context!!)
            synchronizer.synchronize()

            val birthdays = getRepository(context).getBirthdaysToday()
            Log.d(TAG, "birthdays today: ${birthdays.joinToString { it.name }}")
            if (birthdays.isNotEmpty()) {
                notify(context, birthdays)
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
}