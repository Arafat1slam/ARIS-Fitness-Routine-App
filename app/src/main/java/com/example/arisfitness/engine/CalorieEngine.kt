package com.example.arisfitness.engine

import com.example.arisfitness.data.model.DailyRoutineEntry
import com.example.arisfitness.data.model.MealEntry
import kotlin.math.roundToInt

object CalorieEngine {

    /**
     * Calculates Basal Metabolic Rate using the Mifflin-St Jeor equation.
     */
    fun calculateBmr(
        weightKg: Float,
        heightCm: Float,
        age: Int,
        gender: String
    ): Float {
        val base = (10f * weightKg) + (6.25f * heightCm) - (5f * age)
        return when (gender.lowercase()) {
            "male" -> base + 5f
            "female" -> base - 161f
            else -> base - 78f // Midpoint between male and female
        }
    }

    /**
     * Calculates Total Daily Energy Expenditure from BMR and activity multiplier.
     */
    fun calculateTdee(
        bmr: Float,
        activityLevel: String
    ): Float {
        val multiplier = when (activityLevel.lowercase()) {
            "sedentary" -> 1.200f
            "light" -> 1.375f
            "moderate" -> 1.550f
            "active" -> 1.725f
            "very_active" -> 1.900f
            else -> 1.550f
        }
        return bmr * multiplier
    }

    /**
     * Calculates the scale factor comparing user's TDEE to character's reference baseline TDEE.
     */
    fun calculateScaleFactor(
        userTdee: Float,
        characterReferenceTdee: Float
    ): Float {
        if (characterReferenceTdee <= 0f) return 1.0f
        return (userTdee / characterReferenceTdee).coerceIn(0.5f, 2.5f)
    }

    /**
     * Scales an individual meal based on the calculated scale factor.
     */
    fun scaleMeal(meal: MealEntry, scaleFactor: Float): MealEntry {
        val scaledCalories = (meal.baseCalories * scaleFactor).roundToInt()
        val scaledProtein = ((meal.proteinG * scaleFactor * 10f).roundToInt()) / 10f
        val scaledCarbs = ((meal.carbsG * scaleFactor * 10f).roundToInt()) / 10f
        val scaledFat = ((meal.fatG * scaleFactor * 10f).roundToInt()) / 10f

        return meal.copy(
            baseCalories = scaledCalories,
            proteinG = scaledProtein,
            carbsG = scaledCarbs,
            fatG = scaledFat
        )
    }

    /**
     * Scales all meals within a daily routine entry to the user's specific caloric needs.
     */
    fun scaleRoutineEntry(routine: DailyRoutineEntry, scaleFactor: Float): DailyRoutineEntry {
        val scaledMeals = routine.meals.map { scaleMeal(it, scaleFactor) }
        return routine.copy(meals = scaledMeals)
    }
}
