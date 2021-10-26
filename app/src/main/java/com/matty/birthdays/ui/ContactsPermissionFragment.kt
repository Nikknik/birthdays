package com.matty.birthdays.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.matty.birthdays.MainActivity
import com.matty.birthdays.R
import com.matty.birthdays.databinding.FragmentContactsPermissionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsPermissionFragment : Fragment(R.layout.fragment_contacts_permission) {
    private lateinit var binding: FragmentContactsPermissionBinding

    private val requestContactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            importBirthdaysFromContacts()
        } else binding.root.showSnackbar(
            "For the application to work correctly," +
                    "grant the application access to the contacts",
            Snackbar.LENGTH_LONG
        )
    }

    private var hasReadContactsPermission: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hasReadContactsPermission = checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentContactsPermissionBinding.bind(view)
        binding.accessBtn.setOnClickListener {
            requestContactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun importBirthdaysFromContacts() {
        with((requireActivity() as MainActivity)) {
            dispatchNextScreen()
        }
    }
}