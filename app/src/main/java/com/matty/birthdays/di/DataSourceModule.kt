package com.matty.birthdays.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.matty.birthdays.data.BirthdayDatabase
import com.matty.birthdays.data.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): BirthdayDatabase {
        return Room.databaseBuilder(
            appContext,
            BirthdayDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideContentResolver(@ApplicationContext appContext: Context): ContentResolver {
        return appContext.contentResolver
    }

    @Provides
    fun provideContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }
}