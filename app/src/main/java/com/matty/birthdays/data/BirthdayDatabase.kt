package com.matty.birthdays.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

const val DATABASE_NAME = "birthday"

@Database(entities = [Birthday::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class BirthdayDatabase : RoomDatabase() {
    abstract fun birthdayDao(): BirthdayDao
}