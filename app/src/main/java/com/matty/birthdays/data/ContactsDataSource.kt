package com.matty.birthdays.data

import android.content.ContentResolver
import android.provider.ContactsContract
import com.matty.birthdays.utils.parseBirthdayDate
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ContactsDataSource"

@Singleton
class ContactsDataSource @Inject constructor() {

    @Inject
    lateinit var contentResolver: ContentResolver

    fun fetchContacts(): List<Birthday> {
        val uri = ContactsContract.Data.CONTENT_URI
        val queryFields = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Event.START_DATE,
            ContactsContract.Contacts._ID
        )
        val where = ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY

        val cursor = contentResolver.query(uri, queryFields, where, null, null)
        return cursor?.use { c ->
            generateSequence {
                if (c.moveToNext()) c else null
            }.mapNotNull {
                val dateString = it.getString(1)
                parseBirthdayDate(dateString)?.let { birthdayDate ->
                    val (day, month, year) = birthdayDate
                    Birthday(
                        name = it.getString(0),
                        day = day,
                        month = month,
                        year = year,
                        id = it.getLong(2)
                    )
                }
            }.toList()
        } ?: emptyList()
    }
}