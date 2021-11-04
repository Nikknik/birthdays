package com.matty.birthdays.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.data.BirthdayRepository
import com.matty.birthdays.data.ContactsSynchronizer
import com.matty.birthdays.ui.BirthdaysState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

private const val TAG = "BirthdaysViewModel"

@HiltViewModel
class BirthdaysViewModel @Inject constructor(
    birthdayRepository: BirthdayRepository,
    private val contactsSynchronizer: ContactsSynchronizer
) : ViewModel() {

    val birthdaysFlow: Flow<BirthdaysState> = birthdayRepository.getAll()
        // TODO - do it on application launch once. Mb on splash screen
        .onStart { contactsSynchronizer.synchronize() }
        .transform {
            Log.d(TAG, "birthdaysFlow: birthdays received")
            emit(
                Success(
                    birthdays = it.groupByTo(sortedMapOf(), Birthday::nearest)
                )
            )
        }


    fun syncWithContacts() {
        viewModelScope.launch {
            contactsSynchronizer.synchronize()
        }
    }
}

sealed class BirthdaysState {
    object Loading : BirthdaysState()
    data class Success(val birthdays: Map<Date, List<Birthday>>) : BirthdaysState()
}