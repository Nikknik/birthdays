package com.matty.birthdays.ui.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberImagePainter
import com.matty.birthdays.R
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.ui.BirthdaysState
import com.matty.birthdays.ui.BirthdaysState.Loading
import com.matty.birthdays.ui.BirthdaysState.Success
import com.matty.birthdays.ui.theme.BirthdaysTheme
import com.matty.birthdays.ui.view.BirthdaysEmptyView
import com.matty.birthdays.utils.dayOfMonth
import com.matty.birthdays.utils.isReadContactsNotAllowed
import com.matty.birthdays.utils.today
import com.matty.birthdays.utils.tomorrow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.Flow

private val today = today()
private val tomorrow = tomorrow()

@Composable
fun BirthdayListScreen(
    birthdaysFlow: Flow<BirthdaysState>,
    onContactsPermissionGranted: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
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

    val birthdaysState = remember(lifecycleOwner.lifecycle, birthdaysFlow) {
        birthdaysFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }.collectAsState(initial = Loading)

    when (val state = birthdaysState.value) {
        is Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(progress = 0.5f)
            }
        }
        is Success -> {
            if (state.birthdays.isNotEmpty()) {
                BirthdaysView(state.birthdays)
            } else {
                BirthdaysEmptyView(
                    isImportFromContactsVisible = context.isReadContactsNotAllowed(),
                    onImportFromContactsClick = {
                        requestContactsPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BirthdaysView(birthdaysMap: Map<Date, List<Birthday>> = emptyMap()) {
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
                BirthdayRow(birthday = birthday)
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
private fun BirthdayRow(birthday: Birthday) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.White)
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        ) {
            birthday.photoUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                birthday.name,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            birthday.turns?.takeIf { it > 0 }?.let { years ->
                Text(
                    stringResource(R.string.turns, years),
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun BirthdaysView() {
    BirthdaysTheme {
        BirthdaysView(
            mapOf(
                tomorrow to listOf(
                    Birthday(
                        contactId = 1L,
                        name = "Nikita",
                        month = tomorrow.month,
                        day = tomorrow.dayOfMonth,
                        year = 1997
                    ),
                    Birthday(
                        contactId = 1L,
                        name = "Alexander Petrov Alexander Petrov Alexander Petrov Alexander Petrov",
                        month = tomorrow.month,
                        day = tomorrow.dayOfMonth
                    ),
                    Birthday(
                        contactId = 1L,
                        name = "Ivan Ivanov",
                        month = tomorrow.month,
                        day = tomorrow.dayOfMonth
                    )
                )
            )
        )
    }
}