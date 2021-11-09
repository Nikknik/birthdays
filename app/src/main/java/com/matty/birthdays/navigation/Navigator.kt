package com.matty.birthdays.navigation

import com.matty.birthdays.navigation.NavigationEvent.GoBack
import com.matty.birthdays.navigation.NavigationEvent.GoToDestination
import com.matty.birthdays.navigation.Screen.BIRTHDAY_FORM
import com.matty.birthdays.navigation.Screen.BIRTHDAY_LIST
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    private val _mutableNavigationFlow = MutableSharedFlow<NavigationEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigationFlow = _mutableNavigationFlow.asSharedFlow()

    fun goToBirthdayFormScreen(birthdayId: Int = -1) {
        val event = GoToDestination(
            destination = "$BIRTHDAY_FORM/$birthdayId"
        )
        _mutableNavigationFlow.tryEmit(event)
    }

    fun goToBirthdayListScreen() {
        _mutableNavigationFlow.tryEmit(GoToDestination(BIRTHDAY_LIST))
    }

    fun navigateTo(destination: String) {
        val event = GoToDestination(destination)
        _mutableNavigationFlow.tryEmit(event)
    }

    fun goBack() {
        _mutableNavigationFlow.tryEmit(GoBack)
    }
}

sealed class NavigationEvent {
    class GoToDestination(val destination: String) : NavigationEvent()
    object GoBack : NavigationEvent()
}