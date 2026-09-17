package com.example.arisfitness

import com.example.arisfitness.engine.DateResolutionEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DateResolutionEngineTest {

    @Test
    fun testElapsedDaysCalculation() {
        val start = "2026-09-01"
        val today = "2026-09-10"
        val days = DateResolutionEngine.calculateElapsedDays(start, today)
        assertEquals(10, days)
    }

    @Test
    fun testMissedDaysDetection() {
        val start = "2026-09-01"
        val today = "2026-09-05" // 5 elapsed days
        val currentDay = 2        // User is still on day 2
        val paused = emptyList<String>()

        val status = DateResolutionEngine.checkMissedDays(
            startDateStr = start,
            currentDayIndex = currentDay,
            pausedDays = paused,
            completedDayIndices = setOf(1),
            todayStr = today
        )

        assertTrue(status.hasMissedDays)
        assertEquals(3, status.missedCount)
        assertEquals(5, status.expectedCalendarDayIndex)
    }

    @Test
    fun testNoMissedDaysWhenOnSchedule() {
        val start = "2026-09-01"
        val today = "2026-09-01"
        val status = DateResolutionEngine.checkMissedDays(
            startDateStr = start,
            currentDayIndex = 1,
            pausedDays = emptyList(),
            completedDayIndices = emptySet(),
            todayStr = today
        )

        assertFalse(status.hasMissedDays)
        assertEquals(0, status.missedCount)
    }

    @Test
    fun testAdherenceCalculation() {
        val completed = 15
        val elapsed = 20
        val adherence = DateResolutionEngine.calculateAdherence(completed, elapsed)
        assertEquals(75f, adherence, 0.01f)
    }
}
