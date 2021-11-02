package com.matty.birthdays.data

import android.net.Uri
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

    @TypeConverter
    fun toUri(value: String?) = value?.let { Uri.parse(it) }

    @TypeConverter
    fun fromUri(uri: Uri?) = uri?.toString()
}