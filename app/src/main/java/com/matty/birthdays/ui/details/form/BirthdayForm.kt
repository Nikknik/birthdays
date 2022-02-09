package com.matty.birthdays.ui.details.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.matty.birthdays.R
import com.matty.birthdays.data.DateOfBirth
import com.matty.birthdays.ui.details.Photo
import com.matty.birthdays.ui.fields.DateSelectionField
import com.matty.birthdays.ui.fields.FieldError
import com.matty.birthdays.ui.fields.InputField

@Composable
fun BirthdayForm(
    form: BirthdayFormState,
    enabled: Boolean = true
) {
    val (isPhotoDialogOpen, setPhotoDialogOpen) = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Photo(
            uri = form.photoUri,
            size = 120.dp,
            description = if (form.photoUri == null) R.string.add_photo else R.string.photo,
            default = R.drawable.add_photo,
            onClick = { setPhotoDialogOpen(true) }
        )
        PhotoChangeDialog(
            isOpen = isPhotoDialogOpen,
            setDialogOpen = setPhotoDialogOpen,
            uri = form.photoUri,
            onPhotoChanged = { newUri ->
                form.photoUri = newUri
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        NameField(
            field = form.nameField,
            enabled = enabled
        )
        Spacer(modifier = Modifier.height(16.dp))
        DateField(
            field = form.dateField,
            enabled = enabled
        )
    }
}

@Composable
private fun NameField(field: InputField<String>, enabled: Boolean = true) {
    OutlinedTextField(
        value = field.value,
        onValueChange = {
            field.onChange(it)
        },
        label = {
            Text(text = stringResource(R.string.name))
        },
        isError = field.isError,
        leadingIcon = {
            Icon(imageVector = Icons.Outlined.Person, contentDescription = "")
        },
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged(field::onFocusChanged)
    )
    FieldError(field = field)
}

@Composable
private fun DateField(field: InputField<DateOfBirth?>, enabled: Boolean = true) {
    DateSelectionField(
        date = field.value,
        onSelect = field::onChange,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged(field::onFocusChanged),
        isError = field.isError,
        enabled = enabled
    )
    FieldError(field = field)
}