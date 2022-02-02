package com.matty.birthdays.navigation

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.matty.birthdays.ui.LaunchScreen
import com.matty.birthdays.ui.WelcomeScreen
import com.matty.birthdays.ui.details.form.BirthdayFormRouter
import com.matty.birthdays.ui.list.BirthdayListRouter
import com.matty.birthdays.ui.settings.SettingsRouter
import com.matty.birthdays.ui.vm.FirstLaunchViewModel

fun NavGraphBuilder.appNavGraph(
    navAdapter: NavAdapter,
    context: Context,
    isFirstLaunch: MutableState<Boolean>
) {
    composable(Screen.LAUNCH) {
        LaunchScreen(delayMillis = 2500, onDelayFinished = {
            if (isFirstLaunch.value) {
                navAdapter.goToWelcomeScreen()
            } else {
                navAdapter.goToMainScreen()
            }
        })
    }
    composable(Screen.WELCOME) {
        val firstLaunchViewModel = hiltViewModel<FirstLaunchViewModel>()
        WelcomeScreen(onFinish = {
            isFirstLaunch.value = false
            firstLaunchViewModel.scheduleNotifications()
            navAdapter.goToMainScreen()
        })
    }
    composable(Screen.BIRTHDAY_LIST) {
        BirthdayListRouter(navAdapter = navAdapter)
    }
    composable(
        "${Screen.BIRTHDAY_FORM}?$ARG_ID={id}",
        arguments = listOf(navArgument(ARG_ID) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString(ARG_ID)?.toInt()
        BirthdayFormRouter(id = id)
    }
    composable(Screen.SETTINGS) {
        SettingsRouter(navAdapter = navAdapter)
    }
}

object Screen {
    const val LAUNCH = "LAUNCH"
    const val BIRTHDAY_LIST = "BIRTHDAY_LIST"
    const val WELCOME = "CONTACTS_PERMISSION"
    const val BIRTHDAY_FORM = "BIRTHDAY_FORM"
    const val SETTINGS = "SETTINGS"
}

const val ARG_ID = "id"
