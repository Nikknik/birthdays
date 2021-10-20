package com.matty.birthdays

import android.Manifest
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.matty.birthdays.data.BirthdayRepository
import com.matty.birthdays.databinding.ActivityMainBinding
import com.matty.birthdays.navigation.NavigationAware
import com.matty.birthdays.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationAware {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigator: Navigator
    @Inject
    lateinit var repository: BirthdayRepository

    private var contactsSyncObserver: ContentObserver? = null

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

    override fun onDestroy() {
        super.onDestroy()
        //TODO unregister content observers
    }

    fun registerContentObservers() {
        contactsSyncObserver = ContactsSyncObserver(this, repository)
        contentResolver.registerContentObserver(
            ContactsContract.Contacts.CONTENT_URI,
            false,
            contactsSyncObserver!!
        )
    }

    private fun dispatchNextScreen() {
        if (isContactsPermissionGranted()) {
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