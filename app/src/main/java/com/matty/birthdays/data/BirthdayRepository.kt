package com.matty.birthdays.data

import com.matty.birthdays.utils.dayOfMonth
import com.matty.birthdays.utils.month
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BirthdayRepository @Inject constructor() {
    @Inject
    lateinit var contactsDataSource: Lazy<ContactsDataSource>

    @Inject
    lateinit var database: BirthdayDatabase

    fun getAll(): Flow<List<Birthday>> {
        return database.birthdayDao().getBirthdays()
    }

    fun getBirthdaysToday(): List<Birthday> {
        val calendar = Calendar.getInstance()
        return database.birthdayDao().getBirthdays(calendar.dayOfMonth, calendar.month)
    }

    suspend fun addAll(birthdays: List<Birthday>) {
        withContext(Dispatchers.IO) {
            database.birthdayDao().addBirthdays(birthdays)
        }
    }

    fun getFromContacts(id: List<Long> = emptyList()): List<Birthday> {
        return contactsDataSource.get().fetchContacts(id)
    }


    suspend fun deleteById(id: List<Long>) {
        database.birthdayDao().deleteBirthdays(id)
    }
}