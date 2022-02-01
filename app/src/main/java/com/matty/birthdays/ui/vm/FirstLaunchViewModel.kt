package com.matty.birthdays.ui.vm

import androidx.lifecycle.ViewModel
import com.matty.birthdays.notifications.NotificationsScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirstLaunchViewModel @Inject constructor(
    private val notificationsScheduler: NotificationsScheduler
) : ViewModel() {

    fun scheduleNotifications() {
        notificationsScheduler.schedule()
    }
}