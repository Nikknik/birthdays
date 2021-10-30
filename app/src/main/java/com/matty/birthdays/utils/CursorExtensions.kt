package com.matty.birthdays.utils

import android.database.Cursor

fun Cursor.toSequence(): Sequence<Cursor> = generateSequence { if (moveToNext()) this else null }