package com.matty.birthdays.navigation

import androidx.navigation.NavOptionsBuilder
import com.matty.birthdays.navigation.NavigationEvent.GoBack
import com.matty.birthdays.navigation.NavigationEvent.NavigateToRoute
import com.matty.birthdays.navigation.Screen.BIRTHDAY_FORM
import com.matty.birthdays.navigation.Screen.BIRTHDAY_LIST
import com.matty.birthdays.navigation.Screen.WELCOME
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * NavAdapter was created to work with navigation outside of @Composable functions
 * For example it can be usable in ViewModel
 * @see NavFlowHandler
 * */

@Singleton
class NavAdapter @Inject constructor() {
    private val _navigationFlow = MutableSharedFlow<NavigationEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigationFlow = _navigationFlow.asSharedFlow()

    fun goToBirthdayFormScreen(birthdayId: Int? = null) {
        navigateTo(
            route = "$BIRTHDAY_FORM?$ARG_ID=${birthdayId ?: ""}"
        )
    }

    fun goToMainScreen() {
        navigateTo(
            route = BIRTHDAY_LIST,
        ) {
            popUpTo(0)
        }
    }

    fun goToWelcomeScreen() {
        navigateTo(WELCOME)
    }

    fun goBack() {
        _navigationFlow.tryEmit(GoBack)
    }

    private fun navigateTo(route: String, builder: NavOptionsBuilder.() -> Unit = {}) {
        _navigationFlow.tryEmit(
            NavigateToRoute(route, builder)
        )
    }
}

sealed class NavigationEvent {
    object GoBack : NavigationEvent() {
        override fun toString(): String {
            return "Go back"
        }
    }

    class NavigateToRoute(
        val route: String,
        val builder: NavOptionsBuilder.() -> Unit
    ) : NavigationEvent() {
        override fun toString(): String {
            return "Go to route - $route"
        }
    }
}