package com.matty.birthdays.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.data.BirthdayRepository
import com.matty.birthdays.data.ContactsSynchronizer
import com.matty.birthdays.ui.vm.BirthdaysState.Ready
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

private const val TAG = "BirthdaysViewModel"

@HiltViewModel
class BirthdaysViewModel @Inject constructor(
    birthdayRepository: BirthdayRepository,
    private val contactsSynchronizer: ContactsSynchronizer
) : ViewModel() {

    val birthdaysFlow: Flow<BirthdaysState> = birthdayRepository.getAll()
        .onStart { contactsSynchronizer.synchronize() }
        .transform {
            Log.d(TAG, "birthdaysFlow: birthdays received")
            emit(
                Ready(
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
    data class Ready(val birthdays: Map<Date, List<Birthday>>) : BirthdaysState()
}