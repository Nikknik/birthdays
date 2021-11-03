package com.matty.birthdays.data

import android.app.Activity.MODE_PRIVATE
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.provider.ContactsContract.Contacts
import android.provider.ContactsContract.DeletedContacts
import android.util.Log
import com.matty.birthdays.isPermissionGranted
import com.matty.birthdays.utils.toSequence
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private const val PREF_LAST_SYNC_TIMESTAMP = "LAST_SYNC_TIMESTAMP"
private const val TAG = "ContactsBirthdaySync"

@Singleton
class ContactsSynchronizer @Inject constructor() : ContentObserver(null) {
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @ApplicationContext
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var birthdayRepository: BirthdayRepository

    private val sharedPreferences by lazy {
        context.getSharedPreferences(
            context.packageName,
            MODE_PRIVATE
        )
    }

    private val lastSyncTimestamp: Long
        get() = sharedPreferences.getLong(PREF_LAST_SYNC_TIMESTAMP, 0)


    override fun onChange(selfChange: Boolean, uri: Uri?) {
        coroutineScope.launch {
            synchronize()
        }
    }

    suspend fun synchronize() {
        Log.d(TAG, "synchronize: run")

        if (!context.isPermissionGranted(android.Manifest.permission.READ_CONTACTS)) {
            Log.w(TAG, "synchronize: insufficient permissions")
            return
        }

        withContext(coroutineScope.coroutineContext) {
            if (lastSyncTimestamp == 0L) {
                doFirstTimeSynchronization()
            } else {
                syncUpdatedContacts()
                syncDeletedContacts()
            }
        }

        sharedPreferences.edit()
            .putLong(PREF_LAST_SYNC_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }

    private suspend fun syncUpdatedContacts() {
        val contactsId = context.contentResolver.query(
            Contacts.CONTENT_URI,
            arrayOf(Contacts._ID),
            Contacts.CONTACT_LAST_UPDATED_TIMESTAMP + " > $lastSyncTimestamp",
            null,
            null
        )?.use { cursor ->
            cursor.toSequence()
                .map { it.getLong(0) }
                .toList()
        } ?: return

        Log.d(TAG, "syncUpdatedContacts: $contactsId")

        if (contactsId.isEmpty()) {
            return
        }

        birthdayRepository.deleteById(contactsId)

        val updatedBirthdays = birthdayRepository.getFromContacts(contactsId)
        if (updatedBirthdays.isNotEmpty()) {
            birthdayRepository.addAll(updatedBirthdays)
        }
    }

    private suspend fun syncDeletedContacts() {
        val contactsId = context.contentResolver.query(
            DeletedContacts.CONTENT_URI,
            arrayOf(
                DeletedContacts.CONTACT_ID
            ),
            "${DeletedContacts.CONTACT_DELETED_TIMESTAMP} > $lastSyncTimestamp",
            null,
            null
        )?.use { cursor ->
            cursor.toSequence()
                .map {
                    it.getLong(0)
                }.toList()
        } ?: return

        Log.d(TAG, "syncDeletedContacts: $contactsId")

        if (contactsId.isNotEmpty()) {
            birthdayRepository.deleteById(contactsId)
        }
    }

    private suspend fun doFirstTimeSynchronization() {
        val birthdays = birthdayRepository.getFromContacts()
        Log.d(
            TAG,
            "First time sync: importing ${birthdays.size} birthdays from contacts"
        )
        birthdayRepository.addAll(birthdays)
    }
}