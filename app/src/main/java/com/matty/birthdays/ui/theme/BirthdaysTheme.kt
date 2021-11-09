package com.matty.birthdays.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val lightColorPalette = lightColors(
    secondary = PaleRed,
    onSecondary = Color.White
)

@Composable
fun BirthdaysTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = lightColorPalette,
        content = content
    )
}