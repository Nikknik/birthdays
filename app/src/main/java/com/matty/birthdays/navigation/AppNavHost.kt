package com.matty.birthdays.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.matty.birthdays.ui.checkFirstLaunch

private const val TAG = "AppNavHost"

@Composable
fun AppNavHost(
    navController: NavHostController,
    navAdapter: NavAdapter
) {
    val context = LocalContext.current
    val isFirstLaunch = rememberSaveable {
        mutableStateOf(
            context.checkFirstLaunch()
        )
    }

    Log.d(TAG, "AppNavHost: isFirstLaunch: ${isFirstLaunch.value}")

    NavFlowHandler(
        navController = navController,
        navAdapter = navAdapter
    )

    NavHost(
        navController = navController,
        startDestination = Screen.LAUNCH
    ) {
        appNavGraph(
            navAdapter,
            context,
            isFirstLaunch
        )
    }
}