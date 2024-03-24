package com.example.spontaneouschef

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass.
 * Use the [detailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class detailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_details, container, false)

        val button = view.findViewById<Button>(R.id.button3)

        button.setOnClickListener{
            findNavController().navigate(R.id.action_detailsFragment_to_mainFragment)
        }

        return view
    }
}