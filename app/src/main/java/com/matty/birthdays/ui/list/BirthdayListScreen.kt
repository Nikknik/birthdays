package com.matty.birthdays.ui.list

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matty.birthdays.R
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.data.DateOfBirth
import com.matty.birthdays.ui.details.Photo
import com.matty.birthdays.ui.list.BirthdaysState.Loading
import com.matty.birthdays.ui.list.BirthdaysState.Ready
import com.matty.birthdays.ui.theme.BirthdaysTheme
import com.matty.birthdays.utils.isReadContactsNotAllowed
import com.matty.birthdays.utils.today
import com.matty.birthdays.utils.tomorrow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val today = today()
private val tomorrow = tomorrow()

@Composable
fun BirthdayListScreen(
    state: BirthdaysState,
    onContactsPermissionGranted: () -> Unit,
    onAddClicked: () -> Unit = {},
    onRowClicked: (Birthday) -> Unit = {},
    onSettingsClicked: () -> Unit = {}
) {
    val context = LocalContext.current

    val requestContactsPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onContactsPermissionGranted()
        } else
            Toast.makeText(
                context,
                R.string.contacts_permission_rationale,
                Toast.LENGTH_LONG
            ).show()
    }

    Scaffold(
        floatingActionButton = {
            if (state is Ready) {
                FloatingActionButton(onClick = onAddClicked) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                }
            }
        },
        topBar = {
            TopBar(onSettingsClicked)
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (state) {
                is Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(progress = 0.5f)
                    }
                }
                is Ready -> {
                    if (state.birthdays.isNotEmpty()) {
                        BirthdayList(state.birthdays, onRowClicked)
                    } else {
                        BirthdaysEmptyView(
                            isImportFromContactsVisible = context.isReadContactsNotAllowed(),
                            onImportFromContactsClick = {
                                requestContactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    onSettingsClicked: () -> Unit
) {
    TopAppBar(
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp
        ),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier.height(32.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings),
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.clickable {
                    onSettingsClicked()
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BirthdayList(
    birthdaysMap: Map<Date, List<Birthday>> = emptyMap(),
    onRowClicked: (Birthday) -> Unit
) {
    val dateFormat = remember {
        SimpleDateFormat("d MMM", Locale.getDefault())
    }

    LazyColumn(
        modifier =
        Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        birthdaysMap.forEach { (date, birthdays) ->
            stickyHeader(date) {
                DateHeader(date = date, dateFormat = dateFormat)
            }
            items(birthdays) { birthday ->
                BirthdayRow(birthday = birthday, onRowClicked)
            }
        }
    }
}

@Composable
private fun DateHeader(date: Date, dateFormat: SimpleDateFormat) {
    val text = when (date) {
        tomorrow -> stringResource(R.string.tomorrow_short)
        today -> stringResource(R.string.today_text)
        else -> dateFormat.format(date)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0x99E3F2FD)
            )
            .padding(8.dp)
    ) {
        Text(
            text,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.body2
        )
    }
    Divider(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFE3F2FD)
    )
}

@Composable
private fun BirthdayRow(birthday: Birthday, onClick: (Birthday) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.White)
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = {
                onClick.invoke(birthday)
            })
    ) {
        Photo(
            uri = birthday.photoUri,
            size = 40.dp,
            description = R.string.photo
        )
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                birthday.name,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    birthday.turns?.takeIf { it > 0 }?.let { years ->
                        Text(
                            stringResource(R.string.turns, years),
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray
                        )
                    }
                }
                if (birthday.contactId != null) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.from_contacts_label),
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BirthdayList() {
    val now = DateOfBirth.now()
    BirthdaysTheme {
        BirthdayList(
            mapOf(
                tomorrow to listOf(
                    Birthday(
                        contactId = 1L,
                        name = "Nikita",
                        day = now.day,
                        month = now.month,
                        year = now.year?.minus(2)
                    ),
                    Birthday(
                        name = "Alexander Petrov Alexander Petrov Alexander Petrov Alexander Petrov",
                        day = now.day,
                        month = now.month,
                        year = now.year
                    ),
                    Birthday(
                        contactId = 1L,
                        name = "Ivan Ivanov",
                        day = now.day,
                        month = now.month,
                        year = now.year
                    )
                )
            ),
            onRowClicked = {}
        )
    }
}