package com.matty.birthdays.ui.details.form

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.matty.birthdays.R
import com.matty.birthdays.utils.compressPhoto
import com.matty.birthdays.utils.createInternalPhotoUri
import com.matty.birthdays.utils.deletePhoto


@Composable
fun PhotoChangeDialog(
    isOpen: Boolean,
    setDialogOpen: (Boolean) -> Unit,
    uri: Uri?,
    onPhotoChanged: (Uri?) -> Unit
) {
    if (isOpen) {
        val context = LocalContext.current
        val newUri = remember { uri ?: createInternalPhotoUri(context) }

        val takePhotoLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { isPhotoTaken ->
            if (isPhotoTaken) {
                compressPhoto(context, newUri)
                onPhotoChanged(newUri)
                setDialogOpen(false)
            }
        }

        val selectPhotoLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { selectedUri ->
            if (selectedUri != null) {
                compressPhoto(context, selectedUri, newUri)
                onPhotoChanged(newUri)
                setDialogOpen(false)
            }
        }

        AlertDialog(
            onDismissRequest = {
                setDialogOpen(false)
            },
            title = {
                Text(
                    text = stringResource(R.string.change_photo),
                    fontWeight = FontWeight.Bold
                )
            },
            buttons = {
                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                ) {
                    if (uri != null) {
                        Text(
                            text = stringResource(R.string.remove_photo_btn),
                            modifier = Modifier.clickable {
                                if (deletePhoto(context, uri)) {
                                    onPhotoChanged(null)
                                    setDialogOpen(false)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    Text(
                        text = stringResource(R.string.take_photo_btn),
                        modifier = Modifier.clickable {
                            takePhotoLauncher.launch(newUri)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.select_photo_btn),
                        modifier = Modifier.clickable {
                            selectPhotoLauncher.launch("image/*")
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {
                            setDialogOpen(false)
                        }
                    ) {
                        Text(text = stringResource(R.string.cancel_btn))
                    }
                }
            },
            modifier = Modifier.width(400.dp)
        )
    }
}