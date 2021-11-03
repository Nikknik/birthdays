package com.matty.birthdays.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.matty.birthdays.R
import com.matty.birthdays.ui.theme.BirthdaysTheme


@Composable
fun BirthdaysEmptyScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(R.string.no_birthdays))
    }
}

@Preview(showBackground = true)
@Composable
fun BirthdaysEmptyPreview() {
    BirthdaysTheme {
        BirthdaysEmptyScreen()
    }
}