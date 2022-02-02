package com.matty.birthdays.ui.details.form

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matty.birthdays.R
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.data.BirthdayRepository
import com.matty.birthdays.data.DateOfBirth
import com.matty.birthdays.data.getDate
import com.matty.birthdays.navigation.NavAdapter
import com.matty.birthdays.ui.details.form.FormStatus.ERROR
import com.matty.birthdays.ui.details.form.FormStatus.LOADING
import com.matty.birthdays.ui.details.form.FormStatus.READY
import com.matty.birthdays.ui.details.form.FormStatus.SUBMITTING
import com.matty.birthdays.ui.fields.InputField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BirthdayFormViewModel"

@HiltViewModel
class BirthdayFormViewModel @Inject constructor(
    private val birthdayRepository: BirthdayRepository,
    private val navAdapter: NavAdapter
) : ViewModel() {

    private var birthdayId: Int? = null
    private var _status = MutableStateFlow(LOADING)
    val status = _status.asStateFlow()

    var form: BirthdayFormState = BirthdayFormState()
        private set

    fun initFormState(birthdayId: Int?) {
        Log.d(TAG, "initFormState:")
        if (birthdayId == null) {
            initEmptyForm()
        } else {
            initFromBirthday(birthdayId)
        }
    }

    private fun initFromBirthday(birthdayId: Int) {
        this.birthdayId = birthdayId
        viewModelScope.launch {
            val birthday = birthdayRepository.getById(birthdayId)
            form = BirthdayFormState(birthday)
            _status.value = READY
        }
    }

    private fun initEmptyForm() {
        _status.value = READY
    }

    fun onCancelClicked() {
        navAdapter.goBack()
    }

    fun onDoneClicked() {
        if (!form.isValid) {
            return
        }
        _status.value = SUBMITTING
        val birthday = with(form) {
            Birthday(
                id = birthdayId,
                name = nameField.value,
                day = dateField.value!!.day,
                month = dateField.value!!.month,
                year = dateField.value!!.year,
                photoUri = photoUri
            )
        }
        viewModelScope.launch {
            try {
                if (birthdayId == null) {
                    birthdayRepository.add(birthday)
                } else {
                    birthdayRepository.update(birthday)
                }
                navAdapter.goBack()
            } catch (e: Exception) {
                _status.value = ERROR
            }
        }
    }
}

class BirthdayFormState(birthday: Birthday? = null) {
    val nameField: InputField<String> =
        InputField(birthday?.name ?: "", InputField.notEmpty(R.string.form_name_required))
    val dateField: InputField<DateOfBirth?> =
        InputField(birthday?.getDate(), InputField.notNull(R.string.form_date_required))
    var photoUri by mutableStateOf(birthday?.photoUri)
    val isValid: Boolean
        get() = nameField.validate() and dateField.validate()
}

enum class FormStatus {
    LOADING,
    READY,
    SUBMITTING,
    ERROR
}