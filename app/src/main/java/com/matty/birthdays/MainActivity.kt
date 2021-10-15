package com.matty.birthdays

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.matty.birthdays.databinding.ActivityMainBinding
import com.matty.birthdays.navigation.NavigationAware
import com.matty.birthdays.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationAware {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigator = Navigator(supportFragmentManager)

        if (savedInstanceState == null) {
            dispatchNextScreen()
        }
    }

    private fun dispatchNextScreen() {
        if (isContactsPermissionNotGranted()) {
            navigator.toContactsPermissionScreen()
        } else {
            navigator.toBirthdayListScreen()
        }
    }

    override fun getNavigator() = navigator

    private fun isContactsPermissionNotGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CONTACTS
    ) != PackageManager.PERMISSION_GRANTED
}