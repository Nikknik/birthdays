package com.matty.birthdays.ui

import android.app.Activity.MODE_PRIVATE
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.matty.birthdays.navigation.AppNavHost
import com.matty.birthdays.ui.theme.BirthdaysTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            BirthdaysTheme {
                Scaffold { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        AppNavHost(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

private const val PREF_FIRST_LAUNCH = "FIRST_LAUNCH"

fun Context.checkFirstLaunch(): Boolean {
    val sharedPrefs = getSharedPreferences(applicationInfo.packageName, MODE_PRIVATE)
    val isFirstLaunch = sharedPrefs.getBoolean(PREF_FIRST_LAUNCH, true)
    if (isFirstLaunch) {
        sharedPrefs.edit().putBoolean(PREF_FIRST_LAUNCH, false).apply()
    }
    return isFirstLaunch
}
