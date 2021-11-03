package com.matty.birthdays.ui.screen

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matty.birthdays.R

@Composable
fun WelcomeScreen(
    onFinish: () -> Unit
) {
    val context = LocalContext.current

    val requestContactsPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onFinish()
        } else
            Toast.makeText(
                context,
                R.string.contacts_permission_reason,
                Toast.LENGTH_LONG
            ).show()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = stringResource(R.string.greeting),
                style = MaterialTheme.typography.h4
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.grant_access_to_contacts_header),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.allow_access_to_get_birthdays),
                    style = MaterialTheme.typography.caption,
                    color = Color.DarkGray
                )
            }
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_greeting_people),
                contentDescription = stringResource(id = R.string.greeting),
                modifier = Modifier.height(220.dp)
            )
            Button(onClick = {
                requestContactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }) {
                Text(
                    text = stringResource(R.string.grant_access_to_contacts_button)
                )
            }
            TextButton(onClick = onFinish) {
                Text(stringResource(R.string.grant_access_to_contacts_later))
            }
            Text(
                text = stringResource(R.string.privacy_info_label),
                style = MaterialTheme.typography.caption,
                color = Color.DarkGray
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen {}
}