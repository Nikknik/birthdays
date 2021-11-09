package com.matty.birthdays.ui.component.form

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun FieldError(field: InputField<*>) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .padding(start = 16.dp, end = 12.dp)
    ) {
        if (field.isError) {
            Text(
                text = field.error.toString(context),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

sealed class FieldError {
    object Empty : FieldError()
    class RawString(val error: String) : FieldError()
    class StringResource(@StringRes val resId: Int, val args: Array<Any>? = null) : FieldError()

    fun toString(context: Context): String {
        return when (this) {
            is Empty -> ""
            is RawString -> error
            is StringResource -> if (args != null) {
                context.getString(resId, args)
            } else context.getString(resId)
        }
    }
}