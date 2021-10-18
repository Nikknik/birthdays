package com.matty.birthdays.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.matty.birthdays.utils.today
import java.util.Calendar
import java.util.Date

@Entity
data class Birthday(
    @PrimaryKey
    val id: Long,
    val name: String,

    val day: Int,
    val month: Int,
    val year: Int? = null
) : Comparable<Birthday> {
    @Transient
    val nearest: Date = run {
        val calendar = Calendar.getInstance().apply {
            set(
                get(Calendar.YEAR),
                month,
                day,
                0,
                0,
                0
            )
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.time < today()) {
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1)
        }

        calendar.time
    }

    @delegate:Transient
    val willBeYearsOld: Int? by lazy {
        year?.let {
            Calendar.getInstance().apply {
                time = nearest
            }.get(Calendar.YEAR) - it
        }
    }

    override fun compareTo(other: Birthday) = nearest.compareTo(nearest)
}