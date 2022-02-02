package com.matty.birthdays.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.data.BirthdayRepository
import com.matty.birthdays.data.ContactsSynchronizer
import com.matty.birthdays.ui.list.BirthdaysState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

private const val TAG = "BirthdayListViewModel"

@HiltViewModel
class BirthdayListViewModel @Inject constructor(
    private val birthdayRepository: BirthdayRepository,
    private val contactsSynchronizer: ContactsSynchronizer
) : ViewModel() {

    private val _state: MutableStateFlow<BirthdaysState> = MutableStateFlow(Loading)
    val state: StateFlow<BirthdaysState> = _state.asStateFlow()

    fun refreshState() {
        Log.d(TAG, "Refreshing state..")
        _state.value = Loading
        viewModelScope.launch {
            contactsSynchronizer.synchronize()
            _state.update {
                birthdayRepository
                    .getAll()
                    .groupByTo(sortedMapOf(), Birthday::nearest)
                    .let {
                        BirthdaysState.Ready(it)
                    }
            }
        }
    }
}

sealed class BirthdaysState {
    object Loading : BirthdaysState()
    data class Ready(val birthdays: Map<Date, List<Birthday>>) : BirthdaysState()
}