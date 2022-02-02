package com.matty.birthdays.data

import com.matty.birthdays.utils.dayOfMonth
import com.matty.birthdays.utils.month
import dagger.Lazy
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "BirthdayRepository"

@Singleton
class BirthdayRepository @Inject constructor() {
    @Inject
    lateinit var contactsDataSource: Lazy<ContactsDataSource>

    @Inject
    lateinit var database: BirthdayDatabase

    private val dao by lazy { database.birthdayDao() }

    suspend fun getAll(): List<Birthday> {
        return dao.getAll()
    }

    suspend fun getBirthdaysToday(): List<Birthday> {
        val calendar = Calendar.getInstance()
        return database.birthdayDao().getBirthdays(calendar.dayOfMonth, calendar.month)
    }

    suspend fun addAll(birthdays: List<Birthday>) {
        dao.addAll(birthdays)
    }

    suspend fun add(birthday: Birthday) {
        dao.add(birthday)
    }

    suspend fun update(birthday: Birthday) {
        dao.update(birthday)
    }

    fun getFromContacts(id: List<Long> = emptyList()): List<Birthday> {
        return contactsDataSource.get().fetchContacts(id)
    }

    suspend fun getById(id: Int): Birthday? {
        return dao.getById(id)
    }

    suspend fun deleteById(id: List<Long>) {
        database.birthdayDao().deleteById(id)
    }
}