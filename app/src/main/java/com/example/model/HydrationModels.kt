package com.example.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DrinkLog(
    val id: String = System.currentTimeMillis().toString() + "_" + (100..999).random(),
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val label: String = "Verre d'eau"
) {
    val formattedTime: String
        get() {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
}

data class HydrationState(
    val goalMl: Int = 2000,
    val currentIntakeMl: Int = 0,
    val logs: List<DrinkLog> = emptyList(),
    val streakDays: Int = 1
) {
    val progress: Float
        get() = if (goalMl > 0) (currentIntakeMl.toFloat() / goalMl.toFloat()).coerceIn(0f, 1f) else 0f

    val progressPercent: Int
        get() = if (goalMl > 0) ((currentIntakeMl.toFloat() / goalMl.toFloat()) * 100).toInt() else 0

    val remainingMl: Int
        get() = (goalMl - currentIntakeMl).coerceAtLeast(0)

    val isGoalReached: Boolean
        get() = currentIntakeMl >= goalMl

    val currentIntakeLitersFormatted: String
        get() = String.format(Locale.FRANCE, "%.2f", currentIntakeMl / 1000f)

    val goalLitersFormatted: String
        get() = String.format(Locale.FRANCE, "%.1f", goalMl / 1000f)
}
