package com.matty.birthdays.data

import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.core.database.getStringOrNull
import androidx.core.net.toUri
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


private const val TAG = "ContactsDataSource"

@Singleton
class ContactsDataSource @Inject constructor() {

    @Inject
    lateinit var contentResolver: ContentResolver

    fun fetchContacts(id: Collection<Long>): List<Birthday> {
        val uri = ContactsContract.Data.CONTENT_URI
        val queryFields = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Event.START_DATE,
            ContactsContract.CommonDataKinds.Event.CONTACT_ID,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.RawContacts.VERSION
        )
        var where = ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY

        if (id.isNotEmpty()) {
            where = where + " AND " + ContactsContract.CommonDataKinds.Event.CONTACT_ID +
                    " in (${id.joinToString(",")})"
        }

        val cursor = contentResolver.query(uri, queryFields, where, null, null)
        return cursor?.use { c ->
            generateSequence {
                if (c.moveToNext()) c else null
            }.mapNotNull {
                val dateString = it.getString(1)
                parseBirthdayDate(dateString)?.let { birthdayDate ->
                    val (day, month, year) = birthdayDate
                    val contactId = it.getLong(2)
                    Birthday(
                        contactId = contactId,
                        contactVersion = cursor.getInt(4),
                        name = it.getString(0),
                        day = day,
                        month = month,
                        year = year,
                        photoUri = cursor.getStringOrNull(3)?.toUri()
                    )
                }
            }.toList()
        } ?: emptyList()
    }
}

//first - day of month, second - month, third - year (can be null)
private typealias BirthdayDate = Triple<Int, Int, Int?>

private fun parseBirthdayDate(value: String): BirthdayDate? {
    BIRTHDAY_FORMATS.value.forEach {
        try {
            val (format, hasYear) = it
            format.parse(value)?.let { date ->
                val calendar = Calendar.getInstance()
                calendar.time = date
                return BirthdayDate(
                    first = calendar.get(Calendar.DAY_OF_MONTH),
                    second = calendar.get(Calendar.MONTH),
                    third = if (hasYear) {
                        calendar.get(Calendar.YEAR)
                    } else {
                        null
                    }
                )
            }
        } catch (e: Exception) {
        }
    }
    return null
}

private val BIRTHDAY_FORMATS = lazy {
    listOf(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) to true,
        SimpleDateFormat("--MM-dd", Locale.getDefault()) to false
    )
}