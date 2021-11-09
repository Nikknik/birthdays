package com.matty.birthdays.data

import java.util.Calendar

data class DateOfBirth(
    val day: Int,
    val month: Int,
    val year: Int?
) {
    companion object {
        fun now() = Calendar.getInstance().let {
            DateOfBirth(
                day = it.get(Calendar.DAY_OF_MONTH),
                month = it.get(Calendar.MONTH),
                year = it.get(Calendar.YEAR)
            )
        }
    }
}

fun Birthday.getDate() = DateOfBirth(day, month, year)