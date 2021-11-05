package com.matty.birthdays.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


fun Context.isReadContactsNotAllowed() =
    !isPermissionGranted(android.Manifest.permission.READ_CONTACTS)


fun Context.isPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED
