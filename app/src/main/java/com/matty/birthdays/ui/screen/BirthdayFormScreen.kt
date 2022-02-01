package com.matty.birthdays.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matty.birthdays.R
import com.matty.birthdays.ui.component.BirthdaysTopBar
import com.matty.birthdays.ui.component.form.BirthdayForm
import com.matty.birthdays.ui.theme.BirthdaysTheme
import com.matty.birthdays.ui.vm.BirthdayForm
import com.matty.birthdays.ui.vm.FormStatus
import com.matty.birthdays.ui.vm.FormStatus.INIT
import com.matty.birthdays.ui.vm.FormStatus.SUBMITTING


@Composable
fun BirthdayFormScreen(
    onCancelClicked: () -> Unit = {},
    onDoneClicked: () -> Unit = {},
    form: BirthdayForm = BirthdayForm(),
    status: FormStatus = INIT,
    initFormState: suspend () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        initFormState()
    }

    Scaffold(
        topBar = {
            TopBar(onCancelClicked = onCancelClicked, onDoneClicked = onDoneClicked)
        }
    ) {
        if (status == SUBMITTING || status == INIT) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        if (status != INIT) {
            BirthdayForm(
                form,
                enabled = status != SUBMITTING
            )
        }
    }
}

@Composable
private fun TopBar(
    onCancelClicked: () -> Unit,
    onDoneClicked: () -> Unit
) {
    BirthdaysTopBar {
        Icon(
            imageVector = Icons.Outlined.Clear,
            contentDescription = stringResource(R.string.clear),
            modifier = Modifier.clickable {
                onCancelClicked()
            }
        )
        Spacer(modifier = Modifier.width(32.dp))
        Text(
            text = stringResource(R.string.add_birthday),
            modifier = Modifier
                .weight(1f),
            style = MaterialTheme.typography.h6
        )
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = stringResource(R.string.done),
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.clickable(onClick = onDoneClicked)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BirthdayDetailsPreview() {
    BirthdaysTheme {
        BirthdayFormScreen()
    }
}