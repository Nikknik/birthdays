package com.matty.birthdays.data

import android.net.Uri

data class Person(
    val name: String,
    val photoUri: Uri? = null,
    val contactId: Long? = null,
)