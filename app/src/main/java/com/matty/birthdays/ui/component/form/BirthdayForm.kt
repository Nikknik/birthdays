package com.matty.birthdays.ui.component.form

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.matty.birthdays.R
import com.matty.birthdays.data.DateOfBirth
import com.matty.birthdays.ui.component.PhotoDialog
import com.matty.birthdays.ui.vm.BirthdayForm

private val PHOTO_SIZE = 120.dp

@Composable
fun BirthdayForm(
    form: BirthdayForm,
    enabled: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Photo(
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

@Composable
private fun Photo(uri: Uri?, onPhotoChanged: (Uri?) -> Unit) {
    val photoUri = remember { mutableStateOf(uri) }
    val (isPhotoDialogOpen, setPhotoDialogOpen) = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(PHOTO_SIZE)
            .clip(CircleShape)
            .background(Color.LightGray)
            .clickable {
                setPhotoDialogOpen(true)
            },
        contentAlignment = Alignment.Center
    ) {
        val painter =
            if (photoUri.value == null) painterResource(id = R.drawable.add_photo)
            else rememberImagePainter(photoUri.value)

        Image(
            painter = painter,
            contentDescription = stringResource(R.string.add_photo),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        PhotoDialog(
            isOpen = isPhotoDialogOpen,
            setDialogOpen = setPhotoDialogOpen,
            uri = uri,
            onPhotoChanged = { newUri ->
                onPhotoChanged(newUri)
                photoUri.value = newUri
            }
        )
    }
}