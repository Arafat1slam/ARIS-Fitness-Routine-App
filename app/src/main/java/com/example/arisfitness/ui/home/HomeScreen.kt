package com.example.arisfitness.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arisfitness.data.model.CharacterProfile
import com.example.arisfitness.data.model.DailyRoutineEntry
import com.example.arisfitness.data.model.RoutineLogEntry
import com.example.arisfitness.data.model.UserProfile
import com.example.arisfitness.engine.DateResolutionEngine
import com.example.arisfitness.engine.MissedDayStatus
import com.example.arisfitness.theme.ColorCarbs
import com.example.arisfitness.theme.ColorFat
import com.example.arisfitness.theme.ColorProtein
import com.example.arisfitness.theme.CyberViolet
import com.example.arisfitness.theme.DarkBackground
import com.example.arisfitness.theme.DarkCardBorder
import com.example.arisfitness.theme.DarkSurface
import com.example.arisfitness.theme.DarkSurfaceVariant
import com.example.arisfitness.theme.ElectricCyan
import com.example.arisfitness.theme.ManaCyan
import com.example.arisfitness.theme.NeonMint
import com.example.arisfitness.theme.RadiantGold
import com.example.arisfitness.theme.SolarAmber
import com.example.arisfitness.theme.TextMuted
import com.example.arisfitness.theme.TextWhite
import com.example.arisfitness.ui.components.AnimatedCalorieRingCard
import com.example.arisfitness.ui.components.ChecklistItemCard
import com.example.arisfitness.ui.components.MissedDaysDialog
import com.example.arisfitness.ui.components.NextAlarmChip
import com.example.arisfitness.ui.components.StreakFlameBadge
import com.example.arisfitness.ui.components.bouncyClick
import com.example.arisfitness.util.SoundManager
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    routine: DailyRoutineEntry,
    todayLog: RoutineLogEntry?,
    missedDayStatus: MissedDayStatus,
    onToggleItem: (String) -> Unit,
    onUpdateCalories: (Int) -> Unit,
    onHandleMissedDays: (resumeFromSaved: Boolean) -> Unit,
    onNavigateRoutineDetail: () -> Unit,
    onNavigateProgress: () -> Unit,
    onNavigateSettings: () -> Unit
) {
    val context = LocalContext.current
    var showCalorieDialog by remember { mutableStateOf(false) }
    var inputCaloriesText by remember { mutableStateOf("") }

    val character = CharacterProfile.getById(userProfile.selectedCharacterId)
    val accentColor = Color(character.accentColorHex)

    // Calculate completed calories & macros based on completed meals
    val completedMealIds = todayLog?.completedItemIds ?: emptyList()
    val completedMeals = routine.meals.filter { completedMealIds.contains(it.mealId) }
    val autoCaloriesFromMeals = completedMeals.sumOf { it.baseCalories }
    val currentCaloriesConsumed = (todayLog?.customCaloriesConsumed ?: 0) + autoCaloriesFromMeals

    val proteinConsumed = completedMeals.sumOf { it.proteinG.toDouble() }.toFloat()
    val carbsConsumed = completedMeals.sumOf { it.carbsG.toDouble() }.toFloat()
    val fatConsumed = completedMeals.sumOf { it.fatG.toDouble() }.toFloat()

    // Missed Days Handling Dialog
    if (missedDayStatus.hasMissedDays) {
        MissedDaysDialog(
            status = missedDayStatus,
            onResumeFromSaved = { onHandleMissedDays(true) },
            onCatchUpToCalendar = { onHandleMissedDays(false) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 18.dp)
            .verticalScroll(rememberScrollState())
            .padding(top = 26.dp, bottom = 48.dp)
    ) {
        // 1. Top ARIS Branding Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.bouncyClick(scaleDown = 0.96f) {}
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .border(1.5.dp, NeonMint.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = com.example.arisfitness.R.drawable.ic_aris_logo),
                        contentDescription = "ARIS Logo",
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "ARIS FITNESS",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        letterSpacing = 1.4.sp
                    )
                    Text(
                        text = "1,095-DAY ENGINE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonMint,
                        letterSpacing = 1.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.bouncyClick(scaleDown = 0.92f) {}) {
                    StreakFlameBadge(streakDays = userProfile.currentDayIndex)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                        .bouncyClick(scaleDown = 0.90f) { onNavigateSettings() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = TextWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // 2. Active Routine & Character Program Card
        val totalRoutineDays = 1095
        val dayProgressFraction = (routine.dayIndex.toFloat() / totalRoutineDays).coerceIn(0f, 1f)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .bouncyClick(scaleDown = 0.98f) { onNavigateRoutineDetail() },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Character Avatar Badge
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(accentColor.copy(alpha = 0.15f))
                            .border(1.5.dp, accentColor, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        val emoji = when (character.characterId) {
                            "char_titan" -> "🛡️"
                            "char_aero" -> "⚡"
                            "char_ironclad" -> "🔨"
                            "char_shadow" -> "🥷"
                            "char_catalyst" -> "🌱"
                            else -> "⚡"
                        }
                        Text(emoji, fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = character.alias,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = accentColor
                                )
                            }
                            Text(
                                text = "Day ${routine.dayIndex} of 1,095",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = character.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )

                        Text(
                            text = character.focus,
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3-Year Progress Bar
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "3-YEAR PROTOCOL ADHERENCE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "DAY ${routine.dayIndex} / $totalRoutineDays",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = accentColor
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(DarkBackground)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = dayProgressFraction.coerceAtLeast(0.01f))
                                .height(6.dp)
                                .background(
                                    Brush.horizontalGradient(listOf(accentColor, NeonMint))
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Next Alarm / Protocol Chip
        val nextMeal = routine.meals.firstOrNull { !completedMealIds.contains(it.mealId) }
        val nextEvent = nextMeal?.let { "${it.name}" to it.time }
            ?: (routine.workouts.firstOrNull()?.let { it.title to it.time } ?: ("Rest & Sleep Protocol" to routine.sleepTime))
        NextAlarmChip(
            nextEventTitle = nextEvent.first,
            nextEventTime = nextEvent.second,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Futuristic Nutrition & Calorie Ring Card
        AnimatedCalorieRingCard(
            caloriesConsumed = currentCaloriesConsumed,
            calorieTarget = userProfile.tdee.roundToInt(),
            proteinConsumed = proteinConsumed,
            proteinTarget = routine.totalBaseProtein,
            carbsConsumed = carbsConsumed,
            carbsTarget = routine.totalBaseCarbs,
            fatConsumed = fatConsumed,
            fatTarget = routine.totalBaseFat
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 5. Tactile Reactive Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Log Calories Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                DarkSurfaceVariant,
                                NeonMint.copy(alpha = 0.12f)
                            )
                        )
                    )
                    .border(1.dp, NeonMint.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .bouncyClick(scaleDown = 0.94f) {
                        showCalorieDialog = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = NeonMint,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Log Calories",
                        fontSize = 12.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Full Timeline Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkSurfaceVariant)
                    .border(1.dp, ElectricCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .bouncyClick(scaleDown = 0.94f) {
                        onNavigateRoutineDetail()
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Full Timeline",
                        fontSize = 12.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Progress Button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkSurfaceVariant)
                    .border(1.dp, SolarAmber.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .bouncyClick(scaleDown = 0.90f) {
                        onNavigateProgress()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Progress",
                    tint = SolarAmber,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 6. Today's Protocol Checklist Section Header with Progress Indicator
        val totalItems = 1 + routine.meals.size + routine.workouts.size + routine.reminders.size
        val completedCount = (todayLog?.completedItemIds?.size ?: 0)
        val allDone = completedCount == totalItems && totalItems > 0
        val completionPct = if (totalItems > 0) (completedCount * 100) / totalItems else 0

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (allDone) Color(0xFF072115) else DarkSurface
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (allDone) NeonMint else DarkCardBorder
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (allDone) "PROTOCOL CLEARED" else "TODAY'S PROTOCOL CHECKLIST",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = if (allDone) NeonMint else TextWhite,
                                letterSpacing = 1.2.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (allDone) "All objectives completed for Day ${routine.dayIndex}" else "Complete daily objectives to maintain streak",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                if (allDone) NeonMint else DarkSurfaceVariant,
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                if (allDone) NeonMint else DarkCardBorder,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$completedCount of $totalItems Done",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (allDone) Color(0xFF00391A) else if (completedCount > 0) NeonMint else TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Checklist Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(DarkBackground)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = (completedCount.toFloat() / totalItems.coerceAtLeast(1)).coerceIn(0f, 1f))
                            .height(5.dp)
                            .background(
                                Brush.horizontalGradient(listOf(ManaCyan, NeonMint))
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 7. Interactive Checklist Items with Audio Feedback

        // Wake Up Item
        ChecklistItemCard(
            itemId = "wake",
            title = "Wake Up & Hydrate",
            time = routine.wakeTime,
            subtitle = "Initiate circadian rhythm with 500ml water",
            categoryIcon = "☀️",
            categoryColor = SolarAmber,
            isCompleted = todayLog?.completedItemIds?.contains("wake") == true,
            onToggle = onToggleItem,
            detailsText = "Drink 500ml of room-temperature water with electrolytes. Get natural light exposure within 20 minutes of waking.",
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Workouts
        routine.workouts.forEach { w ->
            val isDone = todayLog?.completedItemIds?.contains(w.workoutId) == true
            ChecklistItemCard(
                itemId = w.workoutId,
                title = w.title,
                time = w.time,
                subtitle = "${w.durationMin} min • ${w.category} • ${w.exercises.size} exercises",
                categoryIcon = "🏋️",
                categoryColor = NeonMint,
                isCompleted = isDone,
                onToggle = onToggleItem,
                detailsText = buildString {
                    append(w.notes)
                    if (w.exercises.isNotEmpty()) {
                        append("\n\nExercises:\n")
                        w.exercises.forEach { ex ->
                            append("• ${ex.name} — ${ex.sets} sets × ${ex.reps} (${ex.notes})\n")
                        }
                    }
                },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Meals
        routine.meals.forEach { meal ->
            val isDone = todayLog?.completedItemIds?.contains(meal.mealId) == true
            ChecklistItemCard(
                itemId = meal.mealId,
                title = meal.name,
                time = meal.time,
                subtitle = "${meal.baseCalories} kcal • ${meal.proteinG.roundToInt()}g P • ${meal.carbsG.roundToInt()}g C • ${meal.fatG.roundToInt()}g F",
                categoryIcon = "🥗",
                categoryColor = ElectricCyan,
                isCompleted = isDone,
                onToggle = onToggleItem,
                detailsText = meal.notes.ifBlank { "Nutrient-dense fuel tailored to your TDEE." },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Reminders & Recovery
        routine.reminders.forEach { r ->
            val isDone = todayLog?.completedItemIds?.contains(r.reminderId) == true
            ChecklistItemCard(
                itemId = r.reminderId,
                title = "Habit & Recovery",
                time = r.time,
                subtitle = r.message,
                categoryIcon = "🌙",
                categoryColor = CyberViolet,
                isCompleted = isDone,
                onToggle = onToggleItem,
                detailsText = r.message,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }

    // 8. Quick Calorie Log Dialog with Quick Add Chips
    if (showCalorieDialog) {
        AlertDialog(
            onDismissRequest = { showCalorieDialog = false },
            containerColor = DarkSurface,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text("Log Extra Calories", color = TextWhite, fontWeight = FontWeight.Black, fontSize = 18.sp)
            },
            text = {
                Column {
                    Text("Select quick preset or enter custom calories consumed:", color = TextMuted, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Preset Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(100, 250, 500, 800).forEach { preset ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkSurfaceVariant)
                                    .border(1.dp, NeonMint.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .bouncyClick {
                                        inputCaloriesText = preset.toString()
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+$preset",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonMint
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = inputCaloriesText,
                        onValueChange = { inputCaloriesText = it },
                        placeholder = { Text("e.g. 350", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonMint,
                            unfocusedBorderColor = DarkCardBorder
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeonMint)
                        .bouncyClick {
                            val added = inputCaloriesText.toIntOrNull() ?: 0
                            val newTotal = (todayLog?.customCaloriesConsumed ?: 0) + added
                            onUpdateCalories(newTotal)
                            showCalorieDialog = false
                            inputCaloriesText = ""
                        }
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Save", color = Color(0xFF00391A), fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        SoundManager.playClick(context)
                        showCalorieDialog = false
                    }
                ) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}
