package com.matty.birthdays

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.matty.birthdays.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AppRouter {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dispatchNextFragment()
    }

    private fun dispatchNextFragment() {
        if (isContactsPermissionNotGranted()) {
            navigateToContactsPermissionFragment()
        } else {
            navigateToBirthdaysFragment()
        }
    }

    override fun navigateToContactsPermissionFragment() {
        val birthdayFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (birthdayFragment == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<ContactsPermissionFragment>(R.id.fragment_container)
            }
        }
    }

    override fun navigateToBirthdaysFragment() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<BirthdaysFragment>(R.id.fragment_container)
            }
        }
        if (fragment is ContactsPermissionFragment) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<BirthdaysFragment>(R.id.fragment_container)
            }
        }
    }

    private fun isContactsPermissionNotGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CONTACTS
    ) != PackageManager.PERMISSION_GRANTED
}