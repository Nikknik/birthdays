package com.matty.birthdays

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.matty.birthdays.adapter.BirthdaysAdapter
import com.matty.birthdays.databinding.FragmentBirthdaysBinding
import com.matty.birthdays.ui.BirthdaysViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "BirthdaysFragment"

@AndroidEntryPoint
class BirthdayListFragment : Fragment(R.layout.fragment_birthdays) {

    private val birthdaysViewModel: BirthdaysViewModel by viewModels()
    private lateinit var fragmentBinding: FragmentBirthdaysBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentBinding = FragmentBirthdaysBinding.bind(view)

        with(fragmentBinding) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            birthdaysViewModel.birthdaysLiveData.observe(viewLifecycleOwner) { birthdays ->
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                recyclerView.adapter = BirthdaysAdapter(birthdays)
            }
        }
    }
}