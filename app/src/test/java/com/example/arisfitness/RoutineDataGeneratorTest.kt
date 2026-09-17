package com.example.arisfitness

import com.example.arisfitness.data.model.CharacterProfile
import com.example.arisfitness.data.repository.RoutineDataGenerator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RoutineDataGeneratorTest {

    @Test
    fun testAllFiveCharactersGenerateValidDay1() {
        val characterIds = listOf(
            "char_titan",
            "char_aero",
            "char_ironclad",
            "char_shadow",
            "char_catalyst"
        )

        characterIds.forEach { id ->
            val day1 = RoutineDataGenerator.generateDay(id, 1)
            assertNotNull(day1)
            assertFalse(day1.wakeTime.isBlank())
            assertFalse(day1.sleepTime.isBlank())
            assertTrue(day1.meals.isNotEmpty())
            assertTrue(day1.workouts.isNotEmpty())
            assertTrue(day1.reminders.isNotEmpty())
            assertTrue(day1.totalBaseCalories > 1500)
        }
    }

    @Test
    fun testThreeYearMilestoneDaysGeneration() {
        val testDays = listOf(1, 30, 90, 180, 365, 500, 730, 1000, 1095)
        val character = CharacterProfile.ALL_CHARACTERS[0]

        testDays.forEach { day ->
            val routine = RoutineDataGenerator.generateDay(character.characterId, day)
            assertNotNull(routine)
            assertTrue(routine.dayIndex == day)
            assertFalse(routine.dayTitle.isBlank())
            assertFalse(routine.phase.isBlank())
        }
    }
}
