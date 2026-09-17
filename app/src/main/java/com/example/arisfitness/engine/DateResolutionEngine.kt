package com.example.arisfitness.engine

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.max

data class MissedDayStatus(
    val hasMissedDays: Boolean,
    val missedCount: Int,
    val expectedCalendarDayIndex: Int,
    val currentSavedDayIndex: Int,
    val missedDates: List<String>
)

object DateResolutionEngine {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayDateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.US)
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.US)

    fun getTodayString(): String {
        return dateFormat.format(Date())
    }

    fun getDisplayDate(dateStr: String = getTodayString()): String {
        return try {
            val parsed = dateFormat.parse(dateStr)
            if (parsed != null) displayDateFormat.format(parsed) else dateStr
        } catch (_: Exception) {
            dateStr
        }
    }

    /**
     * Calculates the calendar days between startDate and today.
     * Day 1 is startDate itself.
     */
    fun calculateElapsedDays(startDateStr: String, todayStr: String = getTodayString()): Int {
        if (startDateStr.isBlank()) return 1
        return try {
            val start = dateFormat.parse(startDateStr)
            val today = dateFormat.parse(todayStr)
            if (start != null && today != null) {
                val diffMs = today.time - start.time
                val diffDays = TimeUnit.MILLISECONDS.toDays(diffMs).toInt()
                max(1, diffDays + 1)
            } else {
                1
            }
        } catch (_: Exception) {
            1
        }
    }

    /**
     * Checks if calendar days have elapsed that were neither completed nor formally paused.
     */
    fun checkMissedDays(
        startDateStr: String,
        currentDayIndex: Int,
        pausedDays: List<String>,
        completedDayIndices: Set<Int>,
        todayStr: String = getTodayString()
    ): MissedDayStatus {
        if (startDateStr.isBlank()) {
            return MissedDayStatus(false, 0, 1, 1, emptyList())
        }

        val elapsedCalendarDays = calculateElapsedDays(startDateStr, todayStr)
        val activeExpectedDay = max(1, elapsedCalendarDays - pausedDays.size)

        if (activeExpectedDay > currentDayIndex) {
            val missedCount = activeExpectedDay - currentDayIndex
            val missedDates = mutableListOf<String>()

            // Approximate missing dates
            try {
                val cal = Calendar.getInstance()
                for (i in 1..missedCount) {
                    cal.time = Date()
                    cal.add(Calendar.DAY_OF_YEAR, -i)
                    val dateString = dateFormat.format(cal.time)
                    if (!pausedDays.contains(dateString)) {
                        missedDates.add(dateString)
                    }
                }
            } catch (_: Exception) {}

            return MissedDayStatus(
                hasMissedDays = true,
                missedCount = missedCount,
                expectedCalendarDayIndex = activeExpectedDay.coerceIn(1, 1095),
                currentSavedDayIndex = currentDayIndex,
                missedDates = missedDates
            )
        }

        return MissedDayStatus(
            hasMissedDays = false,
            missedCount = 0,
            expectedCalendarDayIndex = currentDayIndex,
            currentSavedDayIndex = currentDayIndex,
            missedDates = emptyList()
        )
    }

    /**
     * Calculates adherence percentage over total elapsed active days.
     */
    fun calculateAdherence(completedDaysCount: Int, totalElapsedDays: Int): Float {
        if (totalElapsedDays <= 0) return 100f
        val pct = (completedDaysCount.toFloat() / totalElapsedDays.toFloat()) * 100f
        return pct.coerceIn(0f, 100f)
    }
}
