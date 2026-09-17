package com.example.arisfitness.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDetail(
    val name: String,
    val sets: Int,
    val reps: String,
    val notes: String = ""
)

@Serializable
data class WorkoutEntry(
    val workoutId: String,
    val time: String, // HH:mm
    val title: String,
    val durationMin: Int,
    val category: String, // "Strength", "Cardio", "HIIT", "Mobility", "Rest"
    val notes: String = "",
    val exercises: List<ExerciseDetail> = emptyList()
)

@Serializable
data class MealEntry(
    val mealId: String,
    val time: String, // HH:mm
    val name: String,
    val baseCalories: Int,
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float,
    val notes: String = ""
)

@Serializable
data class ReminderEntry(
    val reminderId: String,
    val time: String, // HH:mm
    val message: String,
    val category: String = "general" // "hydration", "recovery", "mindset", "sleep"
)

@Serializable
data class DailyRoutineEntry(
    val characterId: String,
    val dayIndex: Int, // 1..1095
    val dayTitle: String = "Routine Protocol",
    val phase: String = "Foundation",
    val wakeTime: String = "06:00",
    val sleepTime: String = "22:30",
    val meals: List<MealEntry> = emptyList(),
    val workouts: List<WorkoutEntry> = emptyList(),
    val reminders: List<ReminderEntry> = emptyList()
) {
    val totalBaseCalories: Int
        get() = meals.sumOf { it.baseCalories }

    val totalBaseProtein: Float
        get() = meals.sumOf { it.proteinG.toDouble() }.toFloat()

    val totalBaseCarbs: Float
        get() = meals.sumOf { it.carbsG.toDouble() }.toFloat()

    val totalBaseFat: Float
        get() = meals.sumOf { it.fatG.toDouble() }.toFloat()
}
