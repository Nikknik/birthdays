package com.matty.birthdays.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.matty.birthdays.checkFirstLaunch

import com.matty.birthdays.ui.screen.BirthdayListScreen
import com.matty.birthdays.ui.screen.BirthdaysEmptyScreen
import com.matty.birthdays.ui.screen.WelcomeScreen

@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val isFirstLaunch = rememberSaveable {
        mutableStateOf(
            context.checkFirstLaunch()
        )
    }

    NavHost(
        navController = navController,
        startDestination = if (isFirstLaunch.value) Screen.BIRTHDAY_LIST else Screen.WELCOME
    ) {
        composable(Screen.WELCOME) {
            WelcomeScreen(onFinish = {
                navController.toBirthdayListScreen()
            })
        }
        composable(Screen.BIRTHDAY_LIST) {
            BirthdayListScreen(
                viewModel = hiltViewModel(),
                navHostController = navController
            )
        }
        composable(Screen.BIRTHDAYS_EMPTY) {
            BirthdaysEmptyScreen()
        }
    }
}


fun NavHostController.toBirthdaysEmptyScreen() {
    navigate(Screen.BIRTHDAYS_EMPTY)
}

fun NavHostController.toBirthdayListScreen() {
    navigate(Screen.BIRTHDAY_LIST)
}

private object Screen {
    const val BIRTHDAY_LIST = "BIRTHDAY_LIST"
    const val WELCOME = "CONTACTS_PERMISSION"
    const val BIRTHDAYS_EMPTY = "BIRTHDAYS_EMPTY"
}
