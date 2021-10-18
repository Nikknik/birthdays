package com.matty.birthdays.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.matty.birthdays.BirthdayListFragment
import com.matty.birthdays.ContactsPermissionFragment
import com.matty.birthdays.R

private const val TAG = "Navigator"

class Navigator(private val supportFragmentManager: FragmentManager) {

    fun toContactsPermissionScreen() {
        navigateToFragment(ContactsPermissionFragment(), false)
    }

    fun toBirthdayListScreen() {
        navigateToFragment(BirthdayListFragment(), false)
    }

    private fun navigateToFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, fragment)
            if (addToBackStack) {
                addToBackStack(fragment.javaClass.name)
            }
        }
    }
}