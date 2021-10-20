package com.matty.birthdays

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds
import android.provider.ContactsContract.Contacts
import android.provider.ContactsContract.DeletedContacts
import android.util.Log
import com.matty.birthdays.data.BirthdayRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ContactsSyncObserver"

class ContactsSyncObserver(
    private val context: Context,
    private val repository: BirthdayRepository
) : ContentObserver(null) {
    private var lastSyncTimestamp = System.currentTimeMillis()

    init {
        Log.d(TAG, "Initialized")
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        val cnCursor = context.contentResolver.query(
            Contacts.CONTENT_URI,
            arrayOf(
                Contacts._ID,
                Contacts.DISPLAY_NAME,
                Contacts.CONTACT_LAST_UPDATED_TIMESTAMP
            ),
            Contacts.CONTACT_LAST_UPDATED_TIMESTAMP + " > $lastSyncTimestamp",
            null,
            null
        )

        val updated = cnCursor?.use {
            generateSequence {
                if (cnCursor.moveToNext()) cnCursor else null
            }.map {
                cnCursor.getLong(0)
            }.toMutableList()
        } ?: return

        Log.d(TAG, "all updated: $updated")

        //Check birthday exists
        val bCursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(CommonDataKinds.Event.CONTACT_ID),
            CommonDataKinds.Event.TYPE + "=" +
                    CommonDataKinds.Event.TYPE_BIRTHDAY +
                    " and " + CommonDataKinds.Event.CONTACT_ID + " in (" + updated.joinToString(",") + ")",
            null,
            null
        )

        val updatedWithBirthday = bCursor?.use {
            generateSequence {
                if (bCursor.moveToNext()) bCursor else null
            }.map {
                bCursor.getLong(0)
            }.toList()
        } ?: setOf()

        Log.d(TAG, "updated with birthday: $updatedWithBirthday")

        val toDelete = updated.filter { !updatedWithBirthday.contains(it) }.toMutableList()
        updated.removeAll(toDelete)

        Log.d(TAG, "with deleted birthdays: $toDelete")

        //Sync deleted contacts
        val dCursor = context.contentResolver.query(
            DeletedContacts.CONTENT_URI,
            arrayOf(DeletedContacts.CONTACT_ID),
            DeletedContacts.CONTACT_DELETED_TIMESTAMP + " > $lastSyncTimestamp",
            null,
            null
        )
        dCursor?.use {
            while (dCursor.moveToNext()) {
                toDelete.add(dCursor.getLong(0))
            }
        }

        Log.d(TAG, "delete: $toDelete")
        Log.d(TAG, "update: $updated")

        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteBirthdays(toDelete)
            repository.addAll(repository.getFromContacts(updated))
        }

        lastSyncTimestamp = System.currentTimeMillis()
    }
}