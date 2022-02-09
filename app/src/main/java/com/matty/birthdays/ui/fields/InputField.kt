package com.matty.birthdays.ui.fields

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import com.matty.birthdays.ui.fields.FieldError.Empty
import com.matty.birthdays.ui.fields.FieldError.StringResource

typealias FieldValidator<T> = (T) -> FieldError

class InputField<T>(
    initialValue: T,
    private val validator: FieldValidator<T>? = null
) {
    var error by mutableStateOf<FieldError>(Empty)
        private set

    var value by mutableStateOf(initialValue)
        private set

    val isError: Boolean
        get() = error != Empty

    private var focused = false

    fun validate(): Boolean {
        if (isError) {
            return false
        }
        return validator?.let {
            val error = validator.invoke(value)
            return if (error != Empty) {
                this.error = error
                false
            } else {
                true
            }
        } ?: true
    }

    fun onChange(value: T) {
        this.value = value
        validator?.let {
            error = validator.invoke(value)
        }
    }

    fun onFocusChanged(focusState: FocusState) {
        if (focused && !focusState.isFocused && validator != null) {
            error = validator.invoke(value)
        }
        focused = focusState.isFocused
    }

    override fun toString(): String {
        return "(value=$value, error=$error)"
    }

    companion object {
        fun notEmpty(resId: Int, args: Array<Any>? = null) = { value: CharSequence? ->
            if (value == null || value.isEmpty()) {
                StringResource(resId, args)
            } else {
                Empty
            }
        }

        fun notNull(resId: Int, args: Array<Any>? = null) = { value: Any? ->
            if (value == null) {
                StringResource(resId, args)
            } else {
                Empty
            }
        }
    }
}