package com.example.arisfitness.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WeightEntry(
    val date: String, // YYYY-MM-DD
    val weightKg: Float
)

@Serializable
data class UserProfile(
    val userId: String = "user_default",
    val heightCm: Float = 175f,
    val weightKg: Float = 70f,
    val age: Int = 25,
    val gender: String = "male", // "male" | "female" | "other"
    val activityLevel: String = "moderate", // "sedentary" | "light" | "moderate" | "active" | "very_active"
    val bmr: Float = 1680f,
    val tdee: Float = 2350f,
    val selectedCharacterId: String = "char_titan",
    val startDate: String = "", // YYYY-MM-DD
    val currentDayIndex: Int = 1,
    val pausedDays: List<String> = emptyList(),
    val weightHistory: List<WeightEntry> = emptyList()
) {
    val isConfigured: Boolean
        get() = startDate.isNotBlank() && selectedCharacterId.isNotBlank()
}
