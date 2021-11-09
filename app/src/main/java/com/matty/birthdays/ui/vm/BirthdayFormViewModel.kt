package com.matty.birthdays.ui.vm

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
import com.matty.birthdays.navigation.Navigator
import com.matty.birthdays.ui.vm.FormStatus.ERROR
import com.matty.birthdays.ui.vm.FormStatus.READY
import com.matty.birthdays.ui.vm.FormStatus.SUBMITTING
import com.matty.birthdays.ui.component.form.InputField
import com.matty.birthdays.ui.component.form.InputField.Companion.notEmpty
import com.matty.birthdays.ui.component.form.InputField.Companion.notNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayFormViewModel @Inject constructor(
    private val birthdayRepository: BirthdayRepository,
    private val navigator: Navigator
) : ViewModel() {

    private var birthdayId: Int? = null

    var form: BirthdayForm = BirthdayForm()
        private set

    var status by mutableStateOf(FormStatus.INIT)
        private set

    fun initFromBirthday(birthdayId: Int) {
        this.birthdayId = birthdayId
        viewModelScope.launch {
            birthdayRepository.getById(birthdayId).collect { birthday ->
                form = BirthdayForm(of = birthday)
                status = READY
            }
        }
    }

    fun initEmptyForm() {
        status = READY
    }

    fun onDoneClicked() {
        if (!form.isValid) {
            return
        }
        status = SUBMITTING
        val birthday = with(form) {
            Birthday(
                id = birthdayId,
                name = nameField.value,
                day = dateField.value!!.day,
                month = dateField.value!!.month,
                year = dateField.value!!.year
            )
        }
        viewModelScope.launch {
            delay(2000)
            try {
                if (birthdayId == null) {
                    birthdayRepository.add(birthday)
                } else {
                    birthdayRepository.update(birthday)
                }
                navigator.goBack()
            } catch (e: Exception) {
                status = ERROR
            }
        }
    }
}

class BirthdayForm(
    of: Birthday? = null
) {
    val nameField: InputField<String> =
        InputField(of?.name ?: "", notEmpty(R.string.form_name_required))
    val dateField: InputField<DateOfBirth?> =
        InputField(of?.getDate(), notNull(R.string.form_date_required))

    val isValid: Boolean
        get() = nameField.validate() and dateField.validate()
}

enum class FormStatus {
    INIT,
    READY,
    SUBMITTING,
    ERROR
}