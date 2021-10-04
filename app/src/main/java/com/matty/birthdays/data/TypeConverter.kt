package com.matty.birthdays.data

import androidx.room.TypeConverter
import java.util.Date

class TypeConverter {

    @TypeConverter
    fun toDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}