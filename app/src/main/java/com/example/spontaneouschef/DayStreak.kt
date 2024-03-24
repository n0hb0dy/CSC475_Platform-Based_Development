package com.example.spontaneouschef

import java.time.LocalDate

data class DayStreak(
    val days: Int,
    val activeStatus: Int,
    val lastLockinDate: String
)
