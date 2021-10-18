package com.matty.birthdays.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val BIRTHDAY_FORMATS = lazy {
    listOf(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) to true,
        SimpleDateFormat("--MM-dd", Locale.getDefault()) to false
    )
}

//first - day of month, second - month, third - year (can be null)
typealias BirthdayDate = Triple<Int, Int, Int?>

fun parseBirthdayDate(value: String): BirthdayDate? {
    BIRTHDAY_FORMATS.value.forEach {
        try {
            val (format, hasYear) = it
            format.parse(value)?.let { date ->
                val calendar = Calendar.getInstance()
                calendar.time = date
                return BirthdayDate(
                    first = calendar.get(Calendar.DAY_OF_MONTH),
                    second = calendar.get(Calendar.MONTH),
                    third = if (hasYear) {
                        calendar.get(Calendar.YEAR)
                    } else {
                        null
                    }
                )
            }
        } catch (e: Exception) {
        }
    }
    return null
}

fun today(): Date = Calendar.getInstance().apply {
    time = Date()
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.time

fun tomorrow(): Date = Calendar.getInstance().apply {
    time = today()
    set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
}.time