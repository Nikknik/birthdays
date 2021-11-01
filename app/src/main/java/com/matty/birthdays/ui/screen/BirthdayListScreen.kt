package com.matty.birthdays.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matty.birthdays.R
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.ui.theme.PaleRed
import com.matty.birthdays.utils.tomorrow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BirthdayListScreen() {
//    val viewModel: BirthdaysViewModel = viewModel()

}

@Composable
private fun BirthdayListView(birthdays: List<Birthday> = emptyList()) {
    val dateFormat = SimpleDateFormat(
        "d MMM",
        Locale.getDefault()
    )
    val tomorrow: Date = tomorrow()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(birthdays) { birthday ->
            BirthdayItemView(birthday, dateFormat, tomorrow)
        }
    }
}

@Composable
private fun BirthdayItemView(
    birthday: Birthday,
    dateFormat: SimpleDateFormat,
    tomorrow: Date = tomorrow()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(74.dp)
            .clip(RoundedCornerShape(4.dp))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color.LightGray)
                .width(74.dp)
                .fillMaxHeight()
        ) {
            NoAvatarView()
        }

        Text(
            text = birthday.name,
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp)
                .weight(1f),
            color = Color.Black,
            fontSize = 20.sp
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 12.dp, end = 8.dp)
        ) {
            //TODO - refactor
            when {
                birthday.nearest > tomorrow -> {
                    Text(
                        text = dateFormat.format(birthday.nearest),
                        color = PaleRed,
                        fontSize = 20.sp
                    )
                }
                birthday.nearest == tomorrow -> {
                    Text(
                        text = stringResource(R.string.tomorrow_short_text),
                        color = PaleRed,
                        fontSize = 20.sp
                    )
                }
                else -> {
                    Text(
                        text = stringResource(R.string.party_emoji),
                        color = PaleRed,
                        fontSize = 26.sp
                    )
                }
            }

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

@Composable
private fun NoAvatarView() {
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.no_avatar),
        contentDescription = "No avatar",
        colorFilter = tint(color = Color.White)
    )
}

@Preview(showBackground = true)
@Composable
fun BirthdayListView() {
    MaterialTheme {
        BirthdayListView(
            listOf(
                Birthday(
                    id = 1L,
                    name = "Nikita",
                    month = 10,
                    day = 1,
                    year = 1997
                ),
                Birthday(
                    id = 1L,
                    name = "Alexander Petrov",
                    month = 11,
                    day = 2
                ),
                Birthday(
                    id = 1L,
                    name = "Ivan Ivanov",
                    month = 3,
                    day = 8
                )
            )
        )
    }
}