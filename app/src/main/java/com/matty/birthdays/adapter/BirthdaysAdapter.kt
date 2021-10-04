package com.matty.birthdays.adapter

import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.matty.birthdays.R
import com.matty.birthdays.adapter.BirthdaysAdapter.DateHorizon.SOMETIME
import com.matty.birthdays.adapter.BirthdaysAdapter.DateHorizon.TODAY
import com.matty.birthdays.adapter.BirthdaysAdapter.DateHorizon.TOMORROW
import com.matty.birthdays.data.Birthday
import com.matty.birthdays.databinding.ListBirthdayBinding
import com.matty.birthdays.utils.tomorrow
import java.text.SimpleDateFormat
import java.util.Locale

class BirthdaysAdapter(private val items: List<Birthday>) :
    RecyclerView.Adapter<BirthdaysAdapter.BirthdayViewHolder>() {

    private val tomorrow = tomorrow()
    private val dateFormat = SimpleDateFormat(
        "d MMM",
        Locale.getDefault()
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirthdayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BirthdayViewHolder(
            binding = ListBirthdayBinding.inflate(inflater),
            dateFormat = dateFormat
        )
    }

    override fun onBindViewHolder(holder: BirthdayViewHolder, position: Int) {
        val birthday = items[position]
        holder.bind(
            birthday = birthday,
            dateHorizon = when {
                birthday.nearest > tomorrow -> SOMETIME
                birthday.nearest == tomorrow -> TOMORROW
                else -> TODAY
            }
        )
    }

    override fun getItemCount() = items.size

    class BirthdayViewHolder(
        private val binding: ListBirthdayBinding,
        private val dateFormat: SimpleDateFormat
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            private const val PARTY_EMOJI_SIZE = 26f
        }

        fun bind(birthday: Birthday, dateHorizon: DateHorizon) {
            binding.name.text = birthday.name
            when (dateHorizon) {
                SOMETIME -> {
                    binding.date.setText(R.string.tomorrow_short_text)
                }
                TOMORROW -> {
                    binding.date.text = dateFormat.format(birthday.nearest)
                }
                TODAY -> {
                    binding.date.apply {
                        setTextSize(COMPLEX_UNIT_SP, PARTY_EMOJI_SIZE)
                        setText(R.string.party_emoji)
                    }
                }
            }
            birthday.willBeYearsOld?.let { years ->
                binding.turns.text = itemView.context.getString(R.string.turns_text, years)
            }
        }
    }

    enum class DateHorizon {
        TODAY,
        TOMORROW,
        SOMETIME
    }
}