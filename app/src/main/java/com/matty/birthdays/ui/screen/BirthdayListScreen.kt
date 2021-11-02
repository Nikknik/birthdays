package com.matty.birthdays.ui.screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberImagePainter
import com.matty.birthdays.R
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.ui.BirthdayListState.Loading
import com.matty.birthdays.ui.BirthdayListState.Success
import com.matty.birthdays.ui.BirthdaysViewModel
import com.matty.birthdays.ui.theme.BirthdaysTheme
import com.matty.birthdays.ui.theme.PaleRed
import com.matty.birthdays.utils.today
import com.matty.birthdays.utils.tomorrow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private const val TAG = "BirthdayListScreen"

private val today = today()
private val tomorrow = tomorrow()

@Composable
fun BirthdayListScreen(viewModel: BirthdaysViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val birthdaysState = remember(lifecycleOwner.lifecycle) {
        viewModel.birthdaysFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }.collectAsState(initial = Loading)

    when (val state = birthdaysState.value) {
        is Loading -> {
            CircularProgressIndicator()
        }
        is Success -> {
            if (state.birthdays.isNotEmpty()) {
                BirthdayListView(state.birthdays)
            } else {
                BirthdaysEmptyView()
            }
        }
    }


}

@Composable
private fun BirthdaysEmptyView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No birthdays")
    }
}

@Composable
private fun BirthdayListView(birthdays: List<Birthday> = emptyList()) {
    val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())
    Log.d(TAG, "BirthdayListView: render")
    LazyColumn {
        items(birthdays) { birthday ->
            BirthdayItemView(birthday, dateFormat)
        }
    }
}

@Composable
private fun BirthdayItemView(
    birthday: Birthday,
    dateFormat: SimpleDateFormat
) {
    Log.d(TAG, "BirthdayItemView: $birthday")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row {

            ContactImageView(photoUri = birthday.photoUri)

            Text(
                text = birthday.name,
                modifier = Modifier
                    .padding(start = 8.dp, top = 12.dp)
                    .weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontSize = 20.sp
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 12.dp, end = 8.dp)
            ) {
                DateView(
                    date = birthday.nearest,
                    dateFormat = dateFormat
                )

                birthday.turns?.let { years ->
                    Text(
                        text = stringResource(R.string.turns_text, years),
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactImageView(
    photoUri: Uri?,
    background: Color = Color.LightGray
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(background)
            .width(74.dp)
            .height(74.dp)
            .fillMaxHeight()
    ) {
        if (photoUri == null) {
            Image(
                painter = rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.no_avatar)),
                contentDescription = "No avatar",
                colorFilter = tint(color = Color.White)
            )
        } else {
            Image(
                painter = rememberImagePainter(photoUri),
                contentDescription = "Avatar",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun DateView(date: Date, dateFormat: SimpleDateFormat) {
    val (text, fontSize) = when (date) {
        tomorrow -> stringResource(R.string.tomorrow_short_text) to 20.sp
        today -> stringResource(R.string.party_emoji) to 26.sp
        else -> dateFormat.format(date) to 20.sp
    }

    Text(
        text = text,
        color = PaleRed,
        fontSize = fontSize
    )
}

@Preview(showBackground = true)
@Composable
fun BirthdayListView() {
    BirthdaysTheme {
        BirthdayListView(
            listOf(
                Birthday(
                    contactId = 1L,
                    contactVersion = 1,
                    name = "Nikita",
                    month = 10,
                    day = 1,
                    year = 1997
                ),
                Birthday(
                    contactId = 1L,
                    contactVersion = 1,
                    name = "Alexander Petrov Alexander Petrov Alexander Petrov Alexander Petrov",
                    month = 11,
                    day = 2
                ),
                Birthday(
                    contactId = 1L,
                    contactVersion = 1,
                    name = "Ivan Ivanov",
                    month = 3,
                    day = 8
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BirthdaysEmptyPreview() {
    BirthdaysTheme {
        BirthdaysEmptyView()
    }
}