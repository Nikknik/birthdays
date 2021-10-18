package com.matty.birthdays.ui

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(message: String, duration: Int) {
    Snackbar.make(this, message, duration).show()
}