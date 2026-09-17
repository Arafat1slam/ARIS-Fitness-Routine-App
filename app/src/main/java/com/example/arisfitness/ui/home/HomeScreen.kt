package com.example.arisfitness.ui.home

import androidx.compose.animation.AnimatedVisibility
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
            .padding(top = 28.dp, bottom = 48.dp)
    ) {
        // Top ARIS Branding Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.example.arisfitness.R.drawable.ic_aris_logo),
                    contentDescription = "ARIS Logo",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "ARIS FITNESS",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "1,095-DAY ENGINE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonMint,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                StreakFlameBadge(streakDays = userProfile.currentDayIndex)
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = onNavigateSettings) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = TextMuted
                    )
                }
            }
        }

        // Active Character Profile & Hunter Status Bar
        val hunterRank = when (character.characterId) {
            "char_titan" -> "RANK S"
            "char_ironclad" -> "RANK S"
            "char_aero" -> "RANK A"
            "char_shadow" -> "RANK A"
            "char_catalyst" -> "RANK B"
            else -> "RANK S"
        }
        val currentLevel = (userProfile.currentDayIndex / 7) + 1

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Brush.horizontalGradient(listOf(accentColor.copy(alpha = 0.6f), Color.Transparent))
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
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
                                .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .border(1.dp, accentColor.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = hunterRank,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = accentColor,
                                letterSpacing = 0.8.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(DarkSurfaceVariant, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "LV.$currentLevel",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = character.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                    Text(
                        text = "DAY ${routine.dayIndex} / 1,095 • ${character.alias} • ${userProfile.weightKg.roundToInt()}kg",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Next Alarm Chip
        val nextMeal = routine.meals.firstOrNull { !completedMealIds.contains(it.mealId) }
        val nextEvent = nextMeal?.let { "${it.name}" to it.time }
            ?: (routine.workouts.firstOrNull()?.let { it.title to it.time } ?: ("Rest & Wind down" to routine.sleepTime))
        NextAlarmChip(
            nextEventTitle = nextEvent.first,
            nextEventTime = nextEvent.second,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Nutrition & Calorie Ring Card
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

        // Quick Calorie Log & Detailed Timeline Action Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { showCalorieDialog = true },
                modifier = Modifier.weight(1f).height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = NeonMint, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Log Calories", fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = onNavigateRoutineDetail,
                modifier = Modifier.weight(1f).height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Full Timeline", fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
            }

            IconButton(
                onClick = onNavigateProgress,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceVariant)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.BarChart, contentDescription = "Progress", tint = SolarAmber)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Today's Routine Checklist Section - Daily Quest Panel
        val totalItems = 1 + routine.meals.size + routine.workouts.size + routine.reminders.size
        val completedCount = (todayLog?.completedItemIds?.size ?: 0)
        val allDone = completedCount == totalItems && totalItems > 0
        val completionPct = if (totalItems > 0) (completedCount * 100) / totalItems else 0

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (allDone) Color(0xFF072115) else DarkSurfaceVariant
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (allDone) NeonMint else DarkCardBorder
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (allDone) "QUEST CLEARED" else "DAILY QUEST",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = if (allDone) NeonMint else ElectricCyan,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "デイリークエスト",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    }
                    Text(
                        text = if (allDone) "★ ALL OBJECTIVES COMPLETE (+500 EXP) ★" else "PROTOCOL CLEANSING • $completionPct%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (allDone) RadiantGold else TextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            if (allDone) NeonMint else accentColor.copy(alpha = 0.2f),
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            if (allDone) NeonMint else accentColor,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$completedCount / $totalItems",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = if (allDone) Color(0xFF00391A) else TextWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 1. Wake Up Item
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

        // 2. Workouts
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

        // 3. Meals
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

        // 4. Reminders
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

    // Quick Calorie Log Dialog
    if (showCalorieDialog) {
        AlertDialog(
            onDismissRequest = { showCalorieDialog = false },
            containerColor = DarkSurface,
            title = {
                Text("Log Extra Calories", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column {
                    Text("Add extra calories consumed today (snack, drink, etc.):", color = TextMuted, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = inputCaloriesText,
                        onValueChange = { inputCaloriesText = it },
                        placeholder = { Text("e.g. 250", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = NeonMint,
                            unfocusedBorderColor = DarkCardBorder
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val added = inputCaloriesText.toIntOrNull() ?: 0
                        val newTotal = (todayLog?.customCaloriesConsumed ?: 0) + added
                        onUpdateCalories(newTotal)
                        showCalorieDialog = false
                        inputCaloriesText = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonMint)
                ) {
                    Text("Save", color = Color(0xFF00391A), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCalorieDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}
