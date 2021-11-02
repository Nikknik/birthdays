package com.matty.birthdays

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.matty.birthdays.ui.screen.BirthdayListScreen
import com.matty.birthdays.ui.screen.ContactsPermissionScreen
import com.matty.birthdays.ui.theme.BirthdaysTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BirthdaysTheme {
                Scaffold { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        AppNavHost()
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val isContactsPermissionGranted = remember {
        context.isPermissionGranted(Manifest.permission.READ_CONTACTS)
    }

    NavHost(
        navController = navController,
        startDestination = if (isContactsPermissionGranted) Destination.BIRTHDAY_LIST else Destination.CONTACTS_PERMISSION
    ) {
        composable(Destination.CONTACTS_PERMISSION) {
            ContactsPermissionScreen(onPermissionGranted = {
                navController.navigate(
                    Destination.BIRTHDAY_LIST
                )
            })
        }
        composable(Destination.BIRTHDAY_LIST) {
            BirthdayListScreen(
                viewModel = hiltViewModel()
            )
        }
    }
}

fun Context.isPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED

object Destination {
    const val BIRTHDAY_LIST = "BIRTHDAY_LIST"
    const val BIRTHDAY_CALENDAR = "BIRTHDAY_CALENDAR"
    const val CONTACTS_PERMISSION = "CONTACTS_PERMISSION"
}