package com.matty.birthdays.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.matty.birthdays.utils.today
import java.util.Calendar
import java.util.Date

@Entity
data class Birthday(
    val name: String,
    val day: Int,
    val month: Int,
    val year: Int?,
    val photoUri: Uri? = null,
    val contactId: Long? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
) {
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
    val turns: Int? by lazy {
        year?.let {
            Calendar.getInstance().apply {
                time = nearest
            }.get(Calendar.YEAR) - it
        }
    }
}