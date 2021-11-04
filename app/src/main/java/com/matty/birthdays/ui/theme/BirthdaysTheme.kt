package com.matty.birthdays.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

val lightColorPalette = lightColors(

)

@Composable
fun BirthdaysTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = lightColorPalette,
        content = content
    )
}