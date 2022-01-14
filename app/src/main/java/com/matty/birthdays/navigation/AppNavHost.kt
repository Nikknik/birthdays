package com.matty.birthdays.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.matty.birthdays.receiver.NotificationReceiver
import com.matty.birthdays.ui.BirthdaysViewModel
import com.matty.birthdays.ui.checkFirstLaunch
import com.matty.birthdays.ui.screen.BirthdayListScreen
import com.matty.birthdays.ui.screen.LaunchScreen
import com.matty.birthdays.ui.screen.WelcomeScreen

private const val TAG = "AppNavHost"

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    val context = LocalContext.current
    val isFirstLaunch = rememberSaveable {
        mutableStateOf(
            context.checkFirstLaunch()
        )
    }

    Log.d(TAG, "AppNavHost: isFirstLaunch: ${isFirstLaunch.value}")

    NavHost(
        navController = navController,
        startDestination = Screen.LAUNCH_SCREEN
    ) {
        composable(Screen.LAUNCH_SCREEN) {
            LaunchScreen(delayMillis = 2500, onDelayFinished = {
                if (isFirstLaunch.value) {
                    navController.navigate(Screen.WELCOME)
                } else {
                  navController.goToMainScreen()
                }
            })
        }
        composable(Screen.WELCOME) {
            WelcomeScreen(onFinish = {
                isFirstLaunch.value = false
                NotificationReceiver.schedule(context)
                navController.goToMainScreen()
            })
        }
        composable(Screen.BIRTHDAY_LIST) {
            val viewModel = hiltViewModel<BirthdaysViewModel>()
            BirthdayListScreen(
                birthdaysFlow = viewModel.birthdaysFlow,
                onContactsPermissionGranted = viewModel::syncWithContacts
            )
        }
    }
}

private object Screen {
    const val LAUNCH_SCREEN = "LAUNCH_SCREEN"
    const val BIRTHDAY_LIST = "BIRTHDAY_LIST"
    const val WELCOME = "CONTACTS_PERMISSION"
}

private fun NavHostController.goToMainScreen() {
    popBackStack()
    navigate(Screen.BIRTHDAY_LIST)
}