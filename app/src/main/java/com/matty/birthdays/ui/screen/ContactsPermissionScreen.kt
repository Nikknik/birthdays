package com.matty.birthdays.ui.screen

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.matty.birthdays.R

@Composable
fun ContactsPermissionScreen(onPermissionGranted: () -> Unit) {

    val context = LocalContext.current

    val requestContactsPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        } else
            Toast.makeText(
                context,
                "For the application to work correctly," +
                        "grant the application access to the contacts",
                Toast.LENGTH_LONG
            ).show()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.grant_access_to_contacts_header),
            style = MaterialTheme.typography.h5
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.privacy_contacts_label),
            style = MaterialTheme.typography.body2
        )
        Spacer(Modifier.height(20.dp))
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_greeting_people),
            contentDescription = "Greeting",
            modifier = Modifier.height(220.dp)
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = {
            requestContactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }) {
            Text(
                text = stringResource(R.string.grant_access_to_contacts_button)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactsPermissionPreview() {
    ContactsPermissionScreen {}
}