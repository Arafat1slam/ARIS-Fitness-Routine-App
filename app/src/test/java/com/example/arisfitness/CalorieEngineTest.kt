package com.example.arisfitness

import com.example.arisfitness.data.model.MealEntry
import com.example.arisfitness.engine.CalorieEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalorieEngineTest {

    @Test
    fun testMifflinStJeorMaleCalculation() {
        // Male: 70kg, 175cm, 25yo
        // BMR = 10*70 + 6.25*175 - 5*25 + 5
        //     = 700 + 1093.75 - 125 + 5 = 1673.75
        val bmr = CalorieEngine.calculateBmr(70f, 175f, 25, "male")
        assertEquals(1673.75f, bmr, 0.1f)
    }

    @Test
    fun testMifflinStJeorFemaleCalculation() {
        // Female: 60kg, 165cm, 30yo
        // BMR = 10*60 + 6.25*165 - 5*30 - 161
        //     = 600 + 1031.25 - 150 - 161 = 1320.25
        val bmr = CalorieEngine.calculateBmr(60f, 165f, 30, "female")
        assertEquals(1320.25f, bmr, 0.1f)
    }

    @Test
    fun testTdeeCalculationMultipliers() {
        val bmr = 1500f
        assertEquals(1500f * 1.200f, CalorieEngine.calculateTdee(bmr, "sedentary"), 0.1f)
        assertEquals(1500f * 1.375f, CalorieEngine.calculateTdee(bmr, "light"), 0.1f)
        assertEquals(1500f * 1.550f, CalorieEngine.calculateTdee(bmr, "moderate"), 0.1f)
        assertEquals(1500f * 1.725f, CalorieEngine.calculateTdee(bmr, "active"), 0.1f)
        assertEquals(1500f * 1.900f, CalorieEngine.calculateTdee(bmr, "very_active"), 0.1f)
    }

    @Test
    fun testScaleFactorAndMealScaling() {
        val userTdee = 2800f
        val charTdee = 2500f
        val scale = CalorieEngine.calculateScaleFactor(userTdee, charTdee)
        assertEquals(1.12f, scale, 0.01f)

        val meal = MealEntry(
            mealId = "lunch",
            time = "12:30",
            name = "Chicken & Rice",
            baseCalories = 500,
            proteinG = 40f,
            carbsG = 60f,
            fatG = 10f
        )

        val scaled = CalorieEngine.scaleMeal(meal, scale)
        assertEquals(560, scaled.baseCalories)
        assertTrue(scaled.proteinG > 40f)
        assertTrue(scaled.carbsG > 60f)
    }
}
