package com.matty.birthdays

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.matty.birthdays.data.BirthdayRepository
import com.matty.birthdays.databinding.ActivityMainBinding
import com.matty.birthdays.navigation.NavigationAware
import com.matty.birthdays.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationAware {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigator: Navigator

    @Inject
    lateinit var repository: BirthdayRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigator = Navigator(supportFragmentManager)

        if (savedInstanceState == null) {
            dispatchNextScreen()
        }
    }

    override fun getNavigator() = navigator

    fun dispatchNextScreen() {
        if (isContactsPermissionGranted()) {
            binding.progressBar.visibility = View.GONE
            navigator.toBirthdayListScreen()
        } else {
            navigator.toContactsPermissionScreen()
        }
    }

    private fun isContactsPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED
}