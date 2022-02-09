package com.matty.birthdays.ui

import android.app.Activity.MODE_PRIVATE
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.matty.birthdays.navigation.AppNavHost
import com.matty.birthdays.navigation.NavAdapter
import com.matty.birthdays.ui.theme.BirthdaysTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navAdapter: NavAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            BirthdaysTheme {
                AppNavHost(navController = navController, navAdapter = navAdapter)
            }
        }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}

private const val PREF_FIRST_LAUNCH = "FIRST_LAUNCH"

fun Context.checkFirstLaunch(): Boolean {
    val sharedPrefs = getSharedPreferences()
    val isFirstLaunch = sharedPrefs.getBoolean(PREF_FIRST_LAUNCH, true)
    if (isFirstLaunch) {
        sharedPrefs.edit()
            .putBoolean(PREF_FIRST_LAUNCH, false)
            .apply()
    }
    return isFirstLaunch
}

fun Context.getSharedPreferences(): SharedPreferences = getSharedPreferences(
    applicationInfo.packageName,
    MODE_PRIVATE
)