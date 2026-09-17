package com.example.arisfitness.data.repository

import com.example.arisfitness.data.model.DailyRoutineEntry
import com.example.arisfitness.data.model.ExerciseDetail
import com.example.arisfitness.data.model.MealEntry
import com.example.arisfitness.data.model.ReminderEntry
import com.example.arisfitness.data.model.WorkoutEntry

object RoutineDataGenerator {

    /**
     * Generates a fully populated, periodized DailyRoutineEntry for any day 1..1095
     * for any of the 5 character profiles.
     */
    fun generateDay(characterId: String, dayIndex: Int): DailyRoutineEntry {
        val clampedDay = dayIndex.coerceIn(1, 1095)
        val phase = getPhaseTitle(clampedDay)
        val week = ((clampedDay - 1) / 7) + 1
        val dayOfWeek = ((clampedDay - 1) % 7) + 1 // 1..7 (e.g. Day 1 = Mon, Day 7 = Sun)
        val isDeload = (week % 6 == 0) // Every 6th week is a deload/recovery week

        return when (characterId) {
            "char_titan" -> generateTitanDay(clampedDay, dayOfWeek, week, phase, isDeload)
            "char_aero" -> generateAeroDay(clampedDay, dayOfWeek, week, phase, isDeload)
            "char_ironclad" -> generateIroncladDay(clampedDay, dayOfWeek, week, phase, isDeload)
            "char_shadow" -> generateShadowDay(clampedDay, dayOfWeek, week, phase, isDeload)
            "char_catalyst" -> generateCatalystDay(clampedDay, dayOfWeek, week, phase, isDeload)
            else -> generateTitanDay(clampedDay, dayOfWeek, week, phase, isDeload)
        }
    }

    private fun getPhaseTitle(day: Int): String = when {
        day <= 90 -> "Phase I: Structural Foundation"
        day <= 365 -> "Phase II: Progressive Hypertrophy & Overload"
        day <= 730 -> "Phase III: High-Density Intensification"
        else -> "Phase IV: Peak Elite Mastery & Longevity"
    }

    // ==========================================
    // 1. KAELEN VANCE - THE TITAN
    // ==========================================
    private fun generateTitanDay(day: Int, dayOfWeek: Int, week: Int, phase: String, isDeload: Boolean): DailyRoutineEntry {
        val wakeTime = "05:30"
        val sleepTime = "22:00"

        val (title, workouts) = when (dayOfWeek) {
            1 -> "Chest & Triceps Hypertrophy" to listOf(
                WorkoutEntry(
                    workoutId = "titan_w_$day",
                    time = "06:45",
                    title = if (isDeload) "Chest & Triceps (Deload 60% 1RM)" else "Chest & Triceps Hypertrophy (Week $week)",
                    durationMin = if (isDeload) 35 else 55,
                    category = "Strength",
                    notes = "Target 2-3 RIR on top sets. Strict eccentric control.",
                    exercises = listOf(
                        ExerciseDetail("Incline Dumbbell Press", 4, if (isDeload) "8 @ easy" else "8-10 reps", "3 sec negative"),
                        ExerciseDetail("Flat Barbell Bench Press", 3, if (isDeload) "6 reps" else "6-8 reps", "Explosive concentric"),
                        ExerciseDetail("Cable Chest Flyes", 3, "12-15 reps", "Hard peak contraction"),
                        ExerciseDetail("Overhead Tricep Rope Extensions", 4, "10-12 reps", "Full elbow flexion stretch")
                    )
                )
            )
            2 -> "Back & Biceps Thickness" to listOf(
                WorkoutEntry(
                    workoutId = "titan_w_$day",
                    time = "06:45",
                    title = "Back & Biceps Mechanical Tension",
                    durationMin = 50,
                    category = "Strength",
                    notes = "Focus on lat drive and scapular retraction.",
                    exercises = listOf(
                        ExerciseDetail("Barbell Bent-Over Row", 4, "8-10 reps", "Torso at 45 degrees"),
                        ExerciseDetail("Neutral-Grip Lat Pulldowns", 3, "10-12 reps", "Pause at bottom"),
                        ExerciseDetail("Chest-Supported T-Bar Rows", 3, "10 reps", "Slow eccentric"),
                        ExerciseDetail("Incline Dumbbell Curls", 3, "12 reps", "Supinate at peak")
                    )
                )
            )
            3 -> "Quad Dominance & Core" to listOf(
                WorkoutEntry(
                    workoutId = "titan_w_$day",
                    time = "06:45",
                    title = "Lower Body: Quad Dominance",
                    durationMin = 60,
                    category = "Strength",
                    notes = "Elevate heels if ankle mobility requires.",
                    exercises = listOf(
                        ExerciseDetail("Barbell Back Squat", 4, "6-8 reps", "Below parallel depth"),
                        ExerciseDetail("Leg Press (Feet Low & Narrow)", 3, "12-15 reps", "Continuous tension"),
                        ExerciseDetail("Walking Dumbbell Lunges", 3, "12 steps/leg", "Upright torso"),
                        ExerciseDetail("Hanging Leg Raises", 3, "15 reps", "Control pelvic tilt")
                    )
                )
            )
            4 -> "Active Recovery & Mobility" to listOf(
                WorkoutEntry(
                    workoutId = "titan_w_$day",
                    time = "07:30",
                    title = "Decompression & Thoracic Mobility",
                    durationMin = 30,
                    category = "Mobility",
                    notes = "Enhance blood flow and synovial fluid exchange.",
                    exercises = listOf(
                        ExerciseDetail("Foam Rolling Lower Back & Quads", 1, "10 min", "Gentle breathing"),
                        ExerciseDetail("World's Greatest Stretch", 3, "5 reps/side", "Dynamic hold")
                    )
                )
            )
            5 -> "Shoulders & Upper Back Density" to listOf(
                WorkoutEntry(
                    workoutId = "titan_w_$day",
                    time = "06:45",
                    title = "Deltoids & Trapezius Protocol",
                    durationMin = 50,
                    category = "Strength",
                    notes = "Strict overhead pressing mechanics.",
                    exercises = listOf(
                        ExerciseDetail("Standing Overhead Barbell Press", 4, "6-8 reps", "Squeeze glutes & core"),
                        ExerciseDetail("Dumbbell Lateral Raises", 4, "15 reps", "Lead with elbows"),
                        ExerciseDetail("Face Pulls with External Rotation", 4, "15-20 reps", "Posterior delt focus")
                    )
                )
            )
            6 -> "Posterior Chain & Hamstrings" to listOf(
                WorkoutEntry(
                    workoutId = "titan_w_$day",
                    time = "07:00",
                    title = "Hamstring & Glute Hypertrophy",
                    durationMin = 55,
                    category = "Strength",
                    notes = "Hinge from the hips, keep neutral spine.",
                    exercises = listOf(
                        ExerciseDetail("Romanian Deadlift (RDL)", 4, "8-10 reps", "Deep hamstring stretch"),
                        ExerciseDetail("Seated Leg Curl", 4, "12 reps", "Pause on contraction"),
                        ExerciseDetail("Standing Calf Raises", 4, "15 reps", "2 sec pause at stretch")
                    )
                )
            )
            else -> "Full Rest & CNS Reset" to listOf(
                WorkoutEntry(
                    workoutId = "titan_w_$day",
                    time = "08:00",
                    title = "Rest Day: 20-min Leisure Nature Walk",
                    durationMin = 20,
                    category = "Rest",
                    notes = "Zero training stress. Rehydrate and refuel."
                )
            )
        }

        val meals = listOf(
            MealEntry("m_breakfast", "06:15", "Titan High-Protein Oats & Eggs", 680, 52f, 75f, 20f, "4 eggs + 80g oats with berries"),
            MealEntry("m_lunch", "12:30", "Grilled Flank Steak with Jasmine Rice & Greens", 850, 65f, 85f, 26f, "220g steak + 1.5 cups cooked rice"),
            MealEntry("m_snack", "16:30", "Whey Isolate Shake & Raw Almonds", 420, 40f, 20f, 18f, "Handful of almonds + 1.5 scoops whey"),
            MealEntry("m_dinner", "19:45", "Pan-Seared Salmon with Sweet Potato", 850, 58f, 70f, 32f, "200g wild salmon + large roasted potato")
        )

        val reminders = listOf(
            ReminderEntry("r_hydrate", "10:00", "Titan Hydration: Drink 1L electrolyte water", "hydration"),
            ReminderEntry("r_postworkout", "14:30", "Check daily protein target (Target: 200g+)", "nutrition"),
            ReminderEntry("r_sleep", "21:30", "Screen off. Magnesium glycinate & cool room (19°C)", "sleep")
        )

        return DailyRoutineEntry(
            characterId = "char_titan",
            dayIndex = day,
            dayTitle = "Day $day: $title",
            phase = phase,
            wakeTime = wakeTime,
            sleepTime = sleepTime,
            meals = meals,
            workouts = workouts,
            reminders = reminders
        )
    }

    // ==========================================
    // 2. ZEPHYR STERLING - AERO
    // ==========================================
    private fun generateAeroDay(day: Int, dayOfWeek: Int, week: Int, phase: String, isDeload: Boolean): DailyRoutineEntry {
        val wakeTime = "05:00"
        val sleepTime = "21:45"

        val (title, workouts) = when (dayOfWeek) {
            1, 4 -> "High-Velocity Intervals & Sprint Mechanics" to listOf(
                WorkoutEntry(
                    workoutId = "aero_w_$day",
                    time = "06:00",
                    title = "Interval Conditioning: Track/Treadmill (Week $week)",
                    durationMin = 45,
                    category = "HIIT",
                    notes = "8 rounds of 400m intervals at 85% VO2 max with 90s walk rest.",
                    exercises = listOf(
                        ExerciseDetail("Dynamic Warm-up & A-Skips", 1, "8 min", "Fast ground contact"),
                        ExerciseDetail("400m High-Pace Interval", 8, "400m each", "Sprint mechanics"),
                        ExerciseDetail("Cooldown Mobility", 1, "5 min", "Hip flexor flush")
                    )
                )
            )
            2, 5 -> "Athletic Power & Kinetic Chain" to listOf(
                WorkoutEntry(
                    workoutId = "aero_w_$day",
                    time = "06:15",
                    title = "Explosive Kinetic Strength",
                    durationMin = 50,
                    category = "Strength",
                    notes = "Focus on velocity of bar movement.",
                    exercises = listOf(
                        ExerciseDetail("Kettlebell Cleans & Push Press", 4, "6 reps", "Explosive hip drive"),
                        ExerciseDetail("Trap Bar Deadlift (Speed Focus)", 4, "5 reps @ 70%", "Max intent"),
                        ExerciseDetail("Plyometric Box Jumps", 4, "5 jumps", "Stick landing softly")
                    )
                )
            )
            3 -> "Zone 2 Aerobic Base & Core Matrix" to listOf(
                WorkoutEntry(
                    workoutId = "aero_w_$day",
                    time = "06:30",
                    title = "Zone 2 Steady State (Heart Rate 130-140 bpm)",
                    durationMin = 50,
                    category = "Cardio",
                    notes = "Conversational pace. Nasal breathing prioritized."
                )
            )
            6 -> "Agility Ladder & Animal Flow Mobility" to listOf(
                WorkoutEntry(
                    workoutId = "aero_w_$day",
                    time = "07:00",
                    title = "Agility & Proprioception Drills",
                    durationMin = 40,
                    category = "Mobility",
                    notes = "Foot speed, ankle stiffness, and multi-planar movement."
                )
            )
            else -> "Active Recovery: Cold Plunge / Contrast Bath" to listOf(
                WorkoutEntry(
                    workoutId = "aero_w_$day",
                    time = "08:30",
                    title = "Contrast Shower & Nervous System Reset",
                    durationMin = 20,
                    category = "Rest",
                    notes = "3 rounds: 3 min warm, 1 min cold."
                )
            )
        }

        val meals = listOf(
            MealEntry("m_breakfast", "05:45", "Aero Energy Bowl: Banana, Chia & Whey", 550, 40f, 75f, 12f, "Quick absorbing complex carbohydrates"),
            MealEntry("m_lunch", "12:00", "Quinoa, Grilled Chicken Breast & Avocado", 750, 55f, 80f, 22f, "High micronutrient density"),
            MealEntry("m_snack", "16:00", "Greek Yogurt with Blueberries & Honey", 350, 30f, 45f, 5f, "Gut-friendly recovery fuel"),
            MealEntry("m_dinner", "19:30", "Turkey Stir-Fry with Soba Noodles & Bok Choy", 850, 52f, 95f, 25f, "Glycogen replenishment for tomorrow's speed")
        )

        val reminders = listOf(
            ReminderEntry("r_breath", "08:00", "Practice 5 min box breathing (4s in, 4s hold, 4s out)", "recovery"),
            ReminderEntry("r_hydrate", "13:00", "Drink 750ml water with sea salt pinch", "hydration"),
            ReminderEntry("r_sleep", "21:15", "Wind down routine. Sleep is the supreme legal performance enhancer.", "sleep")
        )

        return DailyRoutineEntry(
            characterId = "char_aero",
            dayIndex = day,
            dayTitle = "Day $day: $title",
            phase = phase,
            wakeTime = wakeTime,
            sleepTime = sleepTime,
            meals = meals,
            workouts = workouts,
            reminders = reminders
        )
    }

    // ==========================================
    // 3. MARCUS DRAKE - IRONCLAD
    // ==========================================
    private fun generateIroncladDay(day: Int, dayOfWeek: Int, week: Int, phase: String, isDeload: Boolean): DailyRoutineEntry {
        val wakeTime = "06:00"
        val sleepTime = "22:30"

        val (title, workouts) = when (dayOfWeek) {
            1 -> "Heavy Squat Protocol" to listOf(
                WorkoutEntry(
                    workoutId = "iron_w_$day",
                    time = "07:30",
                    title = "Back Squat: Heavy 5x5 System",
                    durationMin = 65,
                    category = "Strength",
                    notes = "Full brace, tight upper back shelf. 3-4 min rest between sets.",
                    exercises = listOf(
                        ExerciseDetail("Competition Back Squat", 5, "5 reps", "Pause 1s in the hole"),
                        ExerciseDetail("Bulgarian Split Squats", 3, "8 reps/leg", "Heavy dumbbells"),
                        ExerciseDetail("Standing Calf Heavy Raises", 4, "10 reps", "Heavy stack")
                    )
                )
            )
            2 -> "Heavy Bench Press & Tricep Power" to listOf(
                WorkoutEntry(
                    workoutId = "iron_w_$day",
                    time = "07:30",
                    title = "Bench Press: 5x5 Peak Force",
                    durationMin = 60,
                    category = "Strength",
                    notes = "Leg drive locked in, arch firmly planted.",
                    exercises = listOf(
                        ExerciseDetail("Competition Bench Press", 5, "5 reps", "Touch and go or 1s pause"),
                        ExerciseDetail("Close-Grip Bench Press", 3, "8 reps", "Tricep power"),
                        ExerciseDetail("Weighted Dips", 3, "6-8 reps", "Slow descent")
                    )
                )
            )
            3 -> "Deadlift Decompression & Mobility" to listOf(
                WorkoutEntry(
                    workoutId = "iron_w_$day",
                    time = "08:00",
                    title = "Active Spine Decompression & Hamstring Floss",
                    durationMin = 25,
                    category = "Mobility",
                    notes = "Decompress lower back and open adductors."
                )
            )
            4 -> "Heavy Deadlift Dominance" to listOf(
                WorkoutEntry(
                    workoutId = "iron_w_$day",
                    time = "07:30",
                    title = "Deadlift: Conventional / Sumo 5x3",
                    durationMin = 70,
                    category = "Strength",
                    notes = "Reset each rep. Pull the slack out of the bar before lift.",
                    exercises = listOf(
                        ExerciseDetail("Barbell Deadlift", 5, "3 reps @ 85%", "Locked lats"),
                        ExerciseDetail("Deficit Deadlifts", 3, "5 reps", "1-inch deficit"),
                        ExerciseDetail("Barbell Shrugs", 4, "10 reps", "Heavy straps allowed")
                    )
                )
            )
            5 -> "Overhead Press & Upper Accessory" to listOf(
                WorkoutEntry(
                    workoutId = "iron_w_$day",
                    time = "07:30",
                    title = "Strict Overhead Press & Heavy Rows",
                    durationMin = 60,
                    category = "Strength",
                    notes = "Lock elbows at top overhead.",
                    exercises = listOf(
                        ExerciseDetail("Strict Standing OHP", 5, "5 reps", "Head through window"),
                        ExerciseDetail("Pendlay Barbell Rows", 4, "6 reps", "Dead stop off floor"),
                        ExerciseDetail("Heavy Farmer's Walks", 4, "40 meters", "Chalk up")
                    )
                )
            )
            else -> "Deep Rest & Structural Fueling" to listOf(
                WorkoutEntry(
                    workoutId = "iron_w_$day",
                    time = "09:00",
                    title = "Complete Rest Protocol",
                    durationMin = 0,
                    category = "Rest",
                    notes = "Tonnage accumulates; growth happens in sleep."
                )
            )
        }

        val meals = listOf(
            MealEntry("m_breakfast", "06:45", "Powerlifter's 5-Egg Scramble, Hashbrowns & Sourdough", 850, 55f, 85f, 32f, "High energy start with dense carbs"),
            MealEntry("m_lunch", "12:30", "Thick Ground Beef (90/10) with Rice & Cheddar", 950, 68f, 95f, 35f, "Saturated fats & dense micronutrients"),
            MealEntry("m_snack", "16:30", "Peanut Butter Bagel & Double Protein Shake", 500, 45f, 50f, 15f, "Fueling the heavy evening recovery"),
            MealEntry("m_dinner", "20:00", "Braised Beef Short Ribs with Mashed Potatoes", 700, 52f, 60f, 28f, "Deep tissue restorative dinner")
        )

        val reminders = listOf(
            ReminderEntry("r_joint", "09:00", "Take joint support (Omega-3 & Collagen)", "recovery"),
            ReminderEntry("r_creatine", "15:00", "5g Creatine Monohydrate with 500ml water", "hydration"),
            ReminderEntry("r_sleep", "22:00", "Bedtime blackout. Protect the spine.", "sleep")
        )

        return DailyRoutineEntry(
            characterId = "char_ironclad",
            dayIndex = day,
            dayTitle = "Day $day: $title",
            phase = phase,
            wakeTime = wakeTime,
            sleepTime = sleepTime,
            meals = meals,
            workouts = workouts,
            reminders = reminders
        )
    }

    // ==========================================
    // 4. ARIA VANCE - SHADOW
    // ==========================================
    private fun generateShadowDay(day: Int, dayOfWeek: Int, week: Int, phase: String, isDeload: Boolean): DailyRoutineEntry {
        val wakeTime = "05:45"
        val sleepTime = "22:15"

        val (title, workouts) = when (dayOfWeek) {
            1 -> "Handstand Balance & Straight Arm Push" to listOf(
                WorkoutEntry(
                    workoutId = "shadow_w_$day",
                    time = "06:30",
                    title = "Inversion Balance & Planche Progressions",
                    durationMin = 50,
                    category = "Strength",
                    notes = "Warm up wrists thoroughly.",
                    exercises = listOf(
                        ExerciseDetail("Wall-Facing Handstand Hold", 5, "45 sec holds", "Point toes & hollow body"),
                        ExerciseDetail("Planche Leans on Floor", 4, "15 sec", "Protracted scapulae"),
                        ExerciseDetail("Pike Push-ups on Parallettes", 3, "8-10 reps", "Control descent")
                    )
                )
            )
            2 -> "Front Lever & High Pull Progressions" to listOf(
                WorkoutEntry(
                    workoutId = "shadow_w_$day",
                    time = "06:30",
                    title = "Gymnastic Rings: Pulling Dynamics",
                    durationMin = 50,
                    category = "Strength",
                    notes = "False grip mastery on wooden rings.",
                    exercises = listOf(
                        ExerciseDetail("Tuck/Straddle Front Lever Holds", 4, "10-15 sec", "Depressed scapulae"),
                        ExerciseDetail("Strict Ring Muscle-Up Practice", 4, "3-5 reps", "Smooth transition"),
                        ExerciseDetail("Archer Pull-ups", 3, "6 reps/side", "Full arm extension")
                    )
                )
            )
            3 -> "Full Body Mobility & Pancake Stretch" to listOf(
                WorkoutEntry(
                    workoutId = "shadow_w_$day",
                    time = "07:00",
                    title = "Hip Flexibility & Spine Articulation",
                    durationMin = 40,
                    category = "Mobility",
                    notes = "Contract-relax methodology for deep hamstring/groin range."
                )
            )
            4 -> "Legs, Pistol Squats & Nordic Curls" to listOf(
                WorkoutEntry(
                    workoutId = "shadow_w_$day",
                    time = "06:30",
                    title = "Unilateral Calisthenic Lower Body",
                    durationMin = 45,
                    category = "Strength",
                    notes = "Single leg balance and knee resilience.",
                    exercises = listOf(
                        ExerciseDetail("Strict Pistol Squats", 4, "8 reps/leg", "Slow cadence"),
                        ExerciseDetail("Nordic Hamstring Curls (Assisted)", 4, "6 reps", "Max eccentric hold"),
                        ExerciseDetail("Tibialis Raises against Wall", 3, "20 reps", "Ankle protection")
                    )
                )
            )
            5 -> "Ring Dips & Core Compression" to listOf(
                WorkoutEntry(
                    workoutId = "shadow_w_$day",
                    time = "06:30",
                    title = "Ring Dips & L-Sit Compression",
                    durationMin = 45,
                    category = "Strength",
                    notes = "Turn rings out at top of every dip.",
                    exercises = listOf(
                        ExerciseDetail("Ring Dips with RTO (Rings Turned Out)", 4, "8 reps", "Full depth"),
                        ExerciseDetail("Parallette L-Sit to V-Sit", 4, "20 sec holds", "Compressed abs"),
                        ExerciseDetail("Dragon Flags", 3, "6-8 reps", "Straight bodyline")
                    )
                )
            )
            else -> "Mindful Walk & Passive Rest" to listOf(
                WorkoutEntry(
                    workoutId = "shadow_w_$day",
                    time = "08:00",
                    title = "Passive Recovery & Joint Oil",
                    durationMin = 20,
                    category = "Rest",
                    notes = "Gentle wrist and shoulder circles."
                )
            )
        }

        val meals = listOf(
            MealEntry("m_breakfast", "06:15", "Shadow Matcha Green Smoothie & Boiled Eggs", 480, 36f, 45f, 16f, "Clean, anti-inflammatory energy"),
            MealEntry("m_lunch", "12:15", "Lemon-Herb Tilapia with Sweet Potato & Asparagus", 620, 48f, 65f, 15f, "Light, easily digestible fuel"),
            MealEntry("m_snack", "16:15", "Apple Slices with Natural Almond Butter", 300, 10f, 35f, 16f, "Sustained glucose levels"),
            MealEntry("m_dinner", "19:30", "Roast Chicken Breast with Mediterranean Salad", 800, 52f, 55f, 24f, "Lean protein with rich polyphenols")
        )

        val reminders = listOf(
            ReminderEntry("r_wrist", "09:30", "Wrist mobility routine: 3 min gentle flexor pulses", "recovery"),
            ReminderEntry("r_posture", "14:00", "Postural reset: Stand tall, retract chin, relax shoulders", "mindset"),
            ReminderEntry("r_sleep", "21:45", "Deep nasal breathing session before sleep", "sleep")
        )

        return DailyRoutineEntry(
            characterId = "char_shadow",
            dayIndex = day,
            dayTitle = "Day $day: $title",
            phase = phase,
            wakeTime = wakeTime,
            sleepTime = sleepTime,
            meals = meals,
            workouts = workouts,
            reminders = reminders
        )
    }

    // ==========================================
    // 5. LEO HAYES - THE CATALYST
    // ==========================================
    private fun generateCatalystDay(day: Int, dayOfWeek: Int, week: Int, phase: String, isDeload: Boolean): DailyRoutineEntry {
        val wakeTime = "06:30"
        val sleepTime = "22:30"

        val (title, workouts) = when (dayOfWeek) {
            1, 3, 5 -> "Fundamental Full Body Circuit" to listOf(
                WorkoutEntry(
                    workoutId = "catalyst_w_$day",
                    time = "07:15",
                    title = "Full Body Vitality Circuit (Week $week)",
                    durationMin = 40,
                    category = "Strength",
                    notes = "Move with steady cadence, maintain smooth breathing.",
                    exercises = listOf(
                        ExerciseDetail("Goblet Squats with Dumbbell", 3, "10-12 reps", "Comfortable range"),
                        ExerciseDetail("Dumbbell Floor Press", 3, "10-12 reps", "Control down"),
                        ExerciseDetail("Seated Cable Rows", 3, "12 reps", "Shoulders back"),
                        ExerciseDetail("Plank Hold on Forearms", 3, "30-45 sec", "Tight glutes")
                    )
                )
            )
            2, 4 -> "Brisk 30-min Step & Sunshine Walk" to listOf(
                WorkoutEntry(
                    workoutId = "catalyst_w_$day",
                    time = "07:30",
                    title = "Cardio Health: Outdoor Walk",
                    durationMin = 30,
                    category = "Cardio",
                    notes = "Get morning sunlight in your eyes for circadian alignment."
                )
            )
            6 -> "Weekend Dynamic Mobility & Stretch" to listOf(
                WorkoutEntry(
                    workoutId = "catalyst_w_$day",
                    time = "08:30",
                    title = "Gentle Full-Body Stretch & Core",
                    durationMin = 25,
                    category = "Mobility",
                    notes = "Loosen hips, back, and neck."
                )
            )
            else -> "Family / Rest & Mental Rejuvenation" to listOf(
                WorkoutEntry(
                    workoutId = "catalyst_w_$day",
                    time = "09:00",
                    title = "Total Rest & Hobby Day",
                    durationMin = 0,
                    category = "Rest",
                    notes = "Consistency is built on adequate psychological rest."
                )
            )
        }

        val meals = listOf(
            MealEntry("m_breakfast", "07:00", "Warm Oatmeal with Cinnamon, Walnuts & Protein", 480, 32f, 60f, 14f, "Heart-healthy fiber and steady morning energy"),
            MealEntry("m_lunch", "12:30", "Turkey, Hummus & Vegetable Whole Grain Wrap", 580, 40f, 65f, 18f, "Balanced macronutrients and clean satiety"),
            MealEntry("m_snack", "16:00", "Cottage Cheese with Sliced Strawberries", 280, 24f, 25f, 6f, "Mid-day protein stabilization"),
            MealEntry("m_dinner", "19:30", "Baked Cod with Steamed Brown Rice & Broccoli", 760, 48f, 75f, 18f, "Light evening digestion and micronutrient rich")
        )

        val reminders = listOf(
            ReminderEntry("r_water", "10:30", "Time for a tall glass of water (500ml)", "hydration"),
            ReminderEntry("r_stand", "14:30", "Take a 2-minute posture break and stretch your legs", "mindset"),
            ReminderEntry("r_sleep", "22:00", "Dim room lights and read for 15 minutes before sleep", "sleep")
        )

        return DailyRoutineEntry(
            characterId = "char_catalyst",
            dayIndex = day,
            dayTitle = "Day $day: $title",
            phase = phase,
            wakeTime = wakeTime,
            sleepTime = sleepTime,
            meals = meals,
            workouts = workouts,
            reminders = reminders
        )
    }
}
