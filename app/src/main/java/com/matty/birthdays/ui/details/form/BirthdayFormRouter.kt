package com.matty.birthdays.ui.details.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.matty.birthdays.ui.details.form.FormStatus.LOADING

@Composable
fun BirthdayFormRouter(
    id: Int?,
    viewModel: BirthdayFormViewModel = hiltViewModel()
) {
    val status by viewModel.status.collectAsState(LOADING)

    BirthdayFormScreen(
        onCancelClicked = viewModel::onCancelClicked,
        onDoneClicked = viewModel::onDoneClicked,
        status = status,
        form = viewModel.form,
        initFormState = { viewModel.initFormState(id) }
    )
}