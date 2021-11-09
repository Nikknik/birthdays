package com.matty.birthdays.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.matty.birthdays.receiver.NotificationReceiver
import com.matty.birthdays.ui.BirthdaysViewModel
import androidx.navigation.navArgument
import com.matty.birthdays.navigation.NavigationEvent.GoToDestination
import com.matty.birthdays.navigation.NavigationEvent.GoBack
import com.matty.birthdays.navigation.Screen.BIRTHDAY_FORM
import com.matty.birthdays.ui.vm.BirthdayFormViewModel
import com.matty.birthdays.ui.vm.BirthdaysViewModel
import com.matty.birthdays.ui.checkFirstLaunch
import com.matty.birthdays.ui.screen.BirthdayFormScreen
import com.matty.birthdays.ui.screen.BirthdayListScreen
import com.matty.birthdays.ui.screen.LaunchScreen
import com.matty.birthdays.ui.screen.WelcomeScreen
import kotlinx.coroutines.flow.collect

private const val TAG = "AppNavHost"

@Composable
fun AppNavHost(
    navController: NavHostController,
    navigator: Navigator
) {
    val context = LocalContext.current
    val isFirstLaunch = rememberSaveable {
        mutableStateOf(
            context.checkFirstLaunch()
        )
    }

    Log.d(TAG, "AppNavHost: isFirstLaunch: ${isFirstLaunch.value}")

    LaunchedEffect(Unit) {
        navigator.navigationFlow.collect { event ->
            when (event) {
                is GoToDestination -> navController.navigate(event.destination)
                is GoBack -> navController.popBackStack()
            }
        }
    }

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
                onContactsPermissionGranted = viewModel::syncWithContacts,
                onAddClicked = {
                    navigator.goToBirthdayFormScreen()
                },
                onRowClicked = { birthday ->
                    navigator.goToBirthdayFormScreen(birthday.id!!)
                }
            )
        }
        composable(
            "$BIRTHDAY_FORM/{id}",
            arguments = listOf(navArgument(ARG_ID) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val formViewModel = hiltViewModel<BirthdayFormViewModel>()
            val id = backStackEntry.arguments?.getInt(ARG_ID)
            BirthdayFormScreen(
                onCancelClicked = { navigator.goBack() },
                onDoneClicked = formViewModel::onDoneClicked,
                status = formViewModel.status,
                form = formViewModel.form,
                initFormState = {
                    if (id != null) {
                        formViewModel.initFromBirthday(id)
                    } else {
                        formViewModel.initEmptyForm()
                    }
                }
            )
        }
    }
}

private object Screen {
    const val LAUNCH_SCREEN = "LAUNCH_SCREEN"
    const val BIRTHDAY_LIST = "BIRTHDAY_LIST"
    const val WELCOME = "CONTACTS_PERMISSION"
    const val BIRTHDAY_FORM = "BIRTHDAY_FORM"
    const val BIRTHDAY_DETAILS = "BIRTHDAY_DETAILS"
}

private fun NavHostController.goToMainScreen() {
    popBackStack()
    navigate(Screen.BIRTHDAY_LIST)
}

private const val ARG_ID = "id"