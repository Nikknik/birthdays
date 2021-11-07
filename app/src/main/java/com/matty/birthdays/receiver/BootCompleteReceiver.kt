package com.matty.birthdays.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

private const val TAG = "BootCompleteReceiver"

class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: started")
        if(intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            NotificationReceiver.schedule(context!!)
        }
    }
}