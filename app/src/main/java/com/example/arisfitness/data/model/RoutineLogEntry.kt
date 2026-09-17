package com.example.arisfitness.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RoutineLogEntry(
    val date: String, // YYYY-MM-DD
    val characterId: String,
    val dayIndex: Int,
    val completedItemIds: List<String> = emptyList(), // "wake", "meal_breakfast", "workout_1", etc.
    val customCaloriesConsumed: Int = 0,
    val isDayCompleted: Boolean = false,
    val notes: String = ""
)
