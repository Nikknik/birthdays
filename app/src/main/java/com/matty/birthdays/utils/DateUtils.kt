package com.matty.birthdays.utils

import java.util.Calendar
import java.util.Date

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

val Date.dayOfMonth: Int
    get() = Calendar.getInstance().apply {
        time = this@dayOfMonth
    }.get(Calendar.DAY_OF_MONTH)

val Calendar.dayOfMonth: Int
    get() = this.get(Calendar.DAY_OF_MONTH)

val Calendar.month: Int
    get() = this.get(Calendar.MONTH)