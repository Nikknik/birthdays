package com.matty.birthdays.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers

private const val TAG = "BirthdaysImporter"

@Singleton
class BirthdaysImporter @Inject constructor() {
    @Inject
    lateinit var birthdayRepository: BirthdayRepository

    fun import(): LiveData<Boolean> = liveData(context = Dispatchers.IO) {
        try {
            val birthdays = birthdayRepository.getFromContacts()
            Log.d(TAG, "$birthdays")
            birthdayRepository.addAll(birthdays)
            emit(true)
        } catch (e: Exception) {
            emit(false)
            Log.e(TAG,  "Error importing birthdays from contacts", e)
        }
    }
}