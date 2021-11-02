package com.matty.birthdays.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.matty.birthdays.ContactsSynchronizer
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.data.BirthdayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform

private const val TAG = "BirthdaysViewModel"

@HiltViewModel
class BirthdaysViewModel @Inject constructor(
    birthdayRepository: BirthdayRepository,
    private val contactsSynchronized: ContactsSynchronizer
) : ViewModel() {

    val birthdaysFlow = birthdayRepository.getAll()
        .onStart { contactsSynchronized.synchronize() }
        .transform {
            Log.d(TAG, it.toString())
            kotlinx.coroutines.delay(5000)
            emit(
                BirthdayListState.Success(
                    birthdays = it.sortedBy(Birthday::nearest)
                )
            )
        }
}

sealed class BirthdayListState {
    object Loading : BirthdayListState()
    data class Success(val birthdays: List<Birthday>) : BirthdayListState()
}