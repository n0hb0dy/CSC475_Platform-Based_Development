package com.example.spontaneouschef

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController

class prefFragment : Fragment() {
    private lateinit var dataBaseHelper: DataBaseHelper
    private lateinit var preferences: Preferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBaseHelper = DataBaseHelper(context)
        preferences = dataBaseHelper.getPreferences()!!

        val view = inflater.inflate(R.layout.fragment_pref, container, false)

        val button = view.findViewById<ImageButton>(R.id.imageButton2)
        val slider1 = view.findViewById<SeekBar>(R.id.seekBar)
        val slider2 = view.findViewById<SeekBar>(R.id.seekBar2)
        val slider5 = view.findViewById<SeekBar>(R.id.seekBar5)
        val slider4 = view.findViewById<SeekBar>(R.id.seekBar4)

        slider1.progress = preferences.calories
        slider2.progress = preferences.carbs
        slider5.progress = preferences.sugar
        slider4.progress = preferences.sodium

        button.setOnClickListener{
            dataBaseHelper.setPreferences("", slider1.progress,
                slider2.progress, slider5.progress, slider4.progress)
            findNavController().navigate(R.id.action_prefFragment_to_mainFragment)
        }

        return view
    }
}