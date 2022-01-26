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

    val dao by lazy { database.birthdayDao() }

    fun getAll(): Flow<List<Birthday>> {
        return dao.getAll()
    }

    fun getBirthdaysToday(): List<Birthday> {
        val calendar = Calendar.getInstance()
        return database.birthdayDao().getBirthdays(calendar.dayOfMonth, calendar.month)
    }

    suspend fun addAll(birthdays: List<Birthday>) {
        withContext(Dispatchers.IO) {
            dao.addAll(birthdays)
        }
    }

    suspend fun add(birthday: Birthday) {
        withContext(Dispatchers.IO) {
            dao.add(birthday)
        }
    }

    suspend fun update(birthday: Birthday) {
        withContext(Dispatchers.IO) {
            dao.update(birthday)
        }
    }

    fun getFromContacts(id: List<Long> = emptyList()): List<Birthday> {
        return contactsDataSource.get().fetchContacts(id)
    }

    fun getById(id: Int): Flow<Birthday?> {
        return dao.getById(id)
    }

    suspend fun deleteById(id: List<Long>) {
        database.birthdayDao().deleteById(id)
    }
}