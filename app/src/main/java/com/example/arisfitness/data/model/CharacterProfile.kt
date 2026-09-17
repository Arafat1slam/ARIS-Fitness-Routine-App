package com.example.arisfitness.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterProfile(
    val characterId: String,
    val name: String,
    val alias: String,
    val description: String,
    val intensityTag: String, // "Beginner" | "Intermediate" | "Advanced"
    val focus: String,
    val referenceTdee: Float,
    val totalDays: Int = 1095,
    val accentColorHex: Long,
    val quote: String
) {
    companion object {
        val ALL_CHARACTERS = listOf(
            CharacterProfile(
                characterId = "char_titan",
                name = "Kaelen Vance",
                alias = "The Titan",
                description = "Dedicated to maximum muscle hypertrophy, mechanical tension, and bulletproof strength architecture.",
                intensityTag = "Advanced",
                focus = "Hypertrophy & Raw Strength",
                referenceTdee = 2800f,
                accentColorHex = 0xFF00E676, // Neon Emerald
                quote = "Discipline is the bridge between goals and accomplishment."
            ),
            CharacterProfile(
                characterId = "char_aero",
                name = "Zephyr Sterling",
                alias = "Aero",
                description = "High-velocity hybrid training blending athletic conditioning, agility drills, and lean functional power.",
                intensityTag = "Intermediate",
                focus = "Agility & Athletic Conditioning",
                referenceTdee = 2500f,
                accentColorHex = 0xFF00E5FF, // Electric Cyan
                quote = "Speed is a byproduct of precision and relentless rhythm."
            ),
            CharacterProfile(
                characterId = "char_ironclad",
                name = "Marcus Drake",
                alias = "Ironclad",
                description = "Heavy compound powerlifting and structural mass. Built for unbreakable fortitude and heavy tonnage.",
                intensityTag = "Advanced",
                focus = "Powerlifting & Heavy Mass",
                referenceTdee = 3000f,
                accentColorHex = 0xFFFF9100, // Solar Amber
                quote = "Under enough pressure, iron turns into steel."
            ),
            CharacterProfile(
                characterId = "char_shadow",
                name = "Aria Vance",
                alias = "Shadow",
                description = "Gymnastic calisthenics, joint longevity, bodyweight mastery, and razor-sharp lean conditioning.",
                intensityTag = "Intermediate",
                focus = "Calisthenics & Joint Mobility",
                referenceTdee = 2200f,
                accentColorHex = 0xFFD500F9, // Cyber Magenta
                quote = "Control your own body weight before you seek to move the world."
            ),
            CharacterProfile(
                characterId = "char_catalyst",
                name = "Leo Hayes",
                alias = "The Catalyst",
                description = "Sustainable habit transformation, aerobic base building, metabolic health, and lifelong vitality.",
                intensityTag = "Beginner",
                focus = "Habit Building & Longevity",
                referenceTdee = 2100f,
                accentColorHex = 0xFF2979FF, // Neon Cobalt
                quote = "Small daily habits repeated 1,095 times reshape destiny."
            )
        )

        fun getById(id: String): CharacterProfile {
            return ALL_CHARACTERS.find { it.characterId == id } ?: ALL_CHARACTERS[0]
        }
    }
}
