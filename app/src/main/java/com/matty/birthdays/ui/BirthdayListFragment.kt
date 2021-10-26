package com.matty.birthdays.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.matty.birthdays.R
import com.matty.birthdays.adapter.BirthdaysAdapter
import com.matty.birthdays.databinding.FragmentBirthdaysBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "BirthdayListFragment"

@AndroidEntryPoint
class BirthdayListFragment : Fragment(R.layout.fragment_birthdays) {

    private val viewModel: BirthdaysViewModel by viewModels()
    private lateinit var fragmentBinding: FragmentBirthdaysBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentBinding = FragmentBirthdaysBinding.bind(view)
        fragmentBinding.recyclerView.layoutManager = LinearLayoutManager(context)

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.birthdaysFlow
                    .collect { birthdays ->
                        with(fragmentBinding) {
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            recyclerView.adapter = BirthdaysAdapter(birthdays)
                        }
                    }
            }
        }
    }
}