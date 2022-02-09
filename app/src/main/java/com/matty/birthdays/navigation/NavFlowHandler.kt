package com.matty.birthdays.navigation

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.matty.birthdays.navigation.NavigationEvent.GoBack
import com.matty.birthdays.navigation.NavigationEvent.NavigateToRoute
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val TAG = "AppRouter"

@Composable
fun NavFlowHandler(
    navController: NavController,
    navAdapter: NavAdapter
) {
    val context = LocalContext.current

    BackHandler {
        navAdapter.goBack()
    }

    LaunchedEffect(Unit) {
        Log.d(TAG, "Init")
        navAdapter.navigationFlow.onEach { event ->
            Log.d(TAG, event.toString())
            when (event) {
                is NavigateToRoute -> {
                    navController.navigate(event.route, event.builder)
                }
                is GoBack -> {
                    val shouldExit = !navController.popBackStack()
                    if (shouldExit) {
                        (context as? Activity)?.finish()
                    }
                }
            }
        }.launchIn(this)
    }
}