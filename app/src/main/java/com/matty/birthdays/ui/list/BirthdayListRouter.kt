package com.matty.birthdays.ui.list

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.matty.birthdays.R
import com.matty.birthdays.navigation.NavAdapter

@Composable
fun BirthdayListRouter(
    navAdapter: NavAdapter,
    viewModel: BirthdayListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshState()
    }

    BirthdayListScreen(
        state = state,
        onContactsPermissionGranted = viewModel::refreshState,
        onAddClicked = {
            navAdapter.goToBirthdayFormScreen()
        },
        onRowClicked = { birthday ->
            if (birthday.contactId == null) {
                navAdapter.goToBirthdayFormScreen(birthday.id)
            } else {
                Toast.makeText(
                    context,
                    R.string.imported_warning,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )
}