package com.example.spontaneouschef

data class Preferences(
    val intolerances: String,
    val calories: Int,
    val carbs: Int,
    val sugar: Int,
    val sodium: Int
)
