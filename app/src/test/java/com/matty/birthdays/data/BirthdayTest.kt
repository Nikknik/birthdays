package com.matty.birthdays.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class BirthdayTest {
    @Test
    fun `should return how many years will be if year not exists`() {
        val expectedYears = 18
        val tomorrow = LocalDate.now().plusDays(1)
        val birthday = Birthday(
            name = "Vasya",
            day = tomorrow.dayOfMonth,
            month = tomorrow.monthValue,
            year = tomorrow.minusYears((expectedYears - 1).toLong()).year
        )

        assertEquals(expectedYears, birthday.turns)
    }

    @Test
    fun `should return null for turns() if year not exists`() {
        val birthday = Birthday(
            name = "Vasya",
            day = 1,
            month = 12,
            year = null
        )

        assertNull(birthday.turns)
    }
}