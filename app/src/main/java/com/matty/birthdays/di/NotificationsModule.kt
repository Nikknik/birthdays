package com.matty.birthdays.di

import android.content.Context
import com.matty.birthdays.notifications.NotificationsScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NotificationsModule {

    @Provides
    @Singleton
    fun provideNotificationsScheduler(@ApplicationContext appContext: Context): NotificationsScheduler {
        return NotificationsScheduler(appContext)
    }
}