package com.example.spontaneouschef

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController

class prefFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pref, container, false)

        val button = view.findViewById<ImageButton>(R.id.imageButton2)

        button.setOnClickListener{
            findNavController().navigate(R.id.action_prefFragment_to_mainFragment)
        }

        return view
    }
}