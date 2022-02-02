package com.matty.birthdays.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matty.birthdays.R
import com.matty.birthdays.ui.theme.BirthdaysTheme

@Composable
fun BirthdaysEmptyView(
    isImportFromContactsVisible: Boolean,
    onImportFromContactsClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.no_birthdays))
        Spacer(modifier = Modifier.height(20.dp))
        if (isImportFromContactsVisible) {
            OutlinedButton(onClick = onImportFromContactsClick) {
                Text(stringResource(R.string.import_from_contacts_btn))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BirthdaysEmptyPreview() {
    BirthdaysTheme {
        BirthdaysEmptyView(true){}
    }
}