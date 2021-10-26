package com.matty.birthdays.ui

import androidx.lifecycle.ViewModel
import com.matty.birthdays.ContactsSynchronizer
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.data.BirthdayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

@HiltViewModel
class BirthdaysViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var birthdayRepository: BirthdayRepository

    @Inject
    lateinit var contactsSynchronized: ContactsSynchronizer

    val birthdaysFlow
        get() = birthdayRepository.getAll()
            .onStart { contactsSynchronized.synchronize() }
            .transform {
                emit(
                    it.sortedBy(Birthday::nearest)
                )
            }
}