package com.matty.birthdays.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.data.BirthdayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BirthdaysViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var birthdayRepository: BirthdayRepository
    val birthdaysLiveData = liveData {
        birthdayRepository
            .getBirthdays()
            .sortedBy(Birthday::nearest)
            .let {
                emit(it)
            }
    }
}