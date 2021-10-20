package com.matty.birthdays.data

import android.content.Context
import android.util.Log
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "BirthdayRepository"

@Singleton
class BirthdayRepository @Inject constructor() {

    @ApplicationContext
    @Inject lateinit var context: Context
    @Inject lateinit var contactsDataSource: Lazy<ContactsDataSource>
    @Inject lateinit var database: BirthdayDatabase

    suspend fun getBirthdays(): List<Birthday> {
        return database.birthdayDao().getBirthdays()
    }

    suspend fun addAll(birthdays: List<Birthday>) {
        withContext(Dispatchers.IO) {
            database.birthdayDao().addBirthdays(birthdays)
        }
    }

    suspend fun getFromContacts(id: List<Long> = emptyList()): List<Birthday> {
        return contactsDataSource.get().fetchContacts(id)
    }

    suspend fun deleteBirthdays(id: List<Long>) {
        database.birthdayDao().deleteBirthdays(id)
    }
}