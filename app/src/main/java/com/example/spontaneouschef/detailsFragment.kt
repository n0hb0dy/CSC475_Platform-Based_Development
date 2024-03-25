package com.example.spontaneouschef

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass.
 * Use the [detailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class detailsFragment : Fragment() {

    private lateinit var dataBaseHelper: DataBaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        dataBaseHelper = DataBaseHelper(context)

        val button = view.findViewById<Button>(R.id.button3)
        val dayText = view.findViewById<TextView>(R.id.textView7)
        val ingredients = view.findViewById<TextView>(R.id.textView10)
        val instructions = view.findViewById<TextView>(R.id.textView12)

        val latestMeal = dataBaseHelper.getDishesCompleted()[0]

        ingredients.text = latestMeal.ingredients
        instructions.text = latestMeal.instructions

        getDayStreak(dataBaseHelper, dayText)

        button.setOnClickListener{
            findNavController().navigate(R.id.action_detailsFragment_to_mainFragment)
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun getDayStreak(databaseHelper: DataBaseHelper, dayText: TextView) {
        val days = databaseHelper.getDayStreak()?.days

        dayText.text = "$days Days!"
    }
}