package com.matty.birthdays.navigation

import android.util.Log
import android.widget.Toast
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
import androidx.navigation.navArgument
import com.matty.birthdays.R
import com.matty.birthdays.navigation.NavigationEvent.GoBack
import com.matty.birthdays.navigation.NavigationEvent.GoToDestination
import com.matty.birthdays.navigation.Screen.BIRTHDAY_FORM
import com.matty.birthdays.ui.checkFirstLaunch
import com.matty.birthdays.ui.screen.BirthdayFormScreen
import com.matty.birthdays.ui.screen.BirthdayListScreen
import com.matty.birthdays.ui.screen.LaunchScreen
import com.matty.birthdays.ui.screen.WelcomeScreen
import com.matty.birthdays.ui.settings.SettingsRouter
import com.matty.birthdays.ui.vm.BirthdayFormViewModel
import com.matty.birthdays.ui.vm.BirthdaysViewModel
import com.matty.birthdays.ui.vm.FirstLaunchViewModel
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
            val firstLaunchViewModel = hiltViewModel<FirstLaunchViewModel>()

            WelcomeScreen(onFinish = {
                isFirstLaunch.value = false
                firstLaunchViewModel.scheduleNotifications()
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
                    if (birthday.contactId == null) {
                        navigator.goToBirthdayFormScreen(birthday.id)
                    } else {
                        Toast.makeText(
                            context,
                            R.string.imported_warning,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                onSettingsClicked = {
                    navigator.goToSettingsScreen()
                }
            )
        }
        composable(
            "$BIRTHDAY_FORM?$ARG_ID={id}",
            arguments = listOf(navArgument(ARG_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val formViewModel = hiltViewModel<BirthdayFormViewModel>()
            val id = backStackEntry.arguments?.getString(ARG_ID)
            BirthdayFormScreen(
                onCancelClicked = { navigator.goBack() },
                onDoneClicked = formViewModel::onDoneClicked,
                status = formViewModel.status,
                form = formViewModel.form,
                initFormState = {
                    if (id != null) {
                        formViewModel.initFromBirthday(id.toInt())
                    } else {
                        formViewModel.initEmptyForm()
                    }
                }
            )
        }
        composable(Screen.SETTINGS) {
            SettingsRouter(navigator = navigator)
        }
    }
}

object Screen {
    const val LAUNCH_SCREEN = "LAUNCH_SCREEN"
    const val BIRTHDAY_LIST = "BIRTHDAY_LIST"
    const val WELCOME = "CONTACTS_PERMISSION"
    const val BIRTHDAY_FORM = "BIRTHDAY_FORM"
    const val SETTINGS = "SETTINGS"
}

private fun NavHostController.goToMainScreen() {
    popBackStack()
    navigate(Screen.BIRTHDAY_LIST)
}

const val ARG_ID = "id"