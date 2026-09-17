package com.example.arisfitness.ui.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arisfitness.data.model.CharacterProfile
import com.example.arisfitness.data.model.DailyRoutineEntry
import com.example.arisfitness.theme.ColorCarbs
import com.example.arisfitness.theme.ColorFat
import com.example.arisfitness.theme.ColorProtein
import com.example.arisfitness.theme.CyberViolet
import com.example.arisfitness.theme.DarkBackground
import com.example.arisfitness.theme.DarkCardBorder
import com.example.arisfitness.theme.DarkSurface
import com.example.arisfitness.theme.DarkSurfaceVariant
import com.example.arisfitness.theme.ElectricCyan
import com.example.arisfitness.theme.NeonMint
import com.example.arisfitness.theme.SolarAmber
import com.example.arisfitness.theme.TextMuted
import com.example.arisfitness.theme.TextWhite
import kotlin.math.roundToInt

@Composable
fun RoutineDetailScreen(
    initialDayIndex: Int,
    characterId: String,
    userTdee: Float,
    onLoadDayRoutine: (dayIndex: Int) -> DailyRoutineEntry,
    onBack: () -> Unit
) {
    var viewDayIndex by remember { mutableIntStateOf(initialDayIndex) }
    val routine = remember(viewDayIndex) { onLoadDayRoutine(viewDayIndex) }
    val character = remember(characterId) { CharacterProfile.getById(characterId) }
    val accentColor = Color(character.accentColorHex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 18.dp)
            .verticalScroll(rememberScrollState())
            .padding(top = 28.dp, bottom = 48.dp)
    ) {
        // Top Navigation Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
            }
            Text(
                text = "DAY PROTOCOL TIMELINE",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                letterSpacing = 1.sp
            )
            Box(modifier = Modifier.size(48.dp)) // Spacer for balance
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Day Selector Bar (Prev, Current Day, Next)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface, RoundedCornerShape(16.dp))
                .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (viewDayIndex > 1) viewDayIndex-- },
                enabled = viewDayIndex > 1
            ) {
                Icon(
                    Icons.Default.ChevronLeft,
                    contentDescription = "Prev Day",
                    tint = if (viewDayIndex > 1) TextWhite else TextMuted.copy(alpha = 0.3f)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "DAY $viewDayIndex OF 1,095",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = accentColor
                )
                Text(
                    text = routine.phase,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }

            IconButton(
                onClick = { if (viewDayIndex < 1095) viewDayIndex++ },
                enabled = viewDayIndex < 1095
            ) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = "Next Day",
                    tint = if (viewDayIndex < 1095) TextWhite else TextMuted.copy(alpha = 0.3f)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Title Header
        Text(
            text = routine.dayTitle,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
        Text(
            text = "Wake: ${routine.wakeTime} • Sleep: ${routine.sleepTime} • Total Base: ${routine.totalBaseCalories} kcal",
            fontSize = 12.sp,
            color = TextMuted,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        // Timeline Items
        // 1. Wake Event
        TimelineCard(
            time = routine.wakeTime,
            title = "Circadian Wake & Hydration",
            category = "WAKE PROTOCOL",
            categoryColor = SolarAmber,
            description = "Awaken at exact scheduled time. 500ml water with sea salt/electrolytes. Direct natural sunlight exposure for 15-20 min."
        )

        // 2. Workouts
        routine.workouts.forEach { w ->
            TimelineCard(
                time = w.time,
                title = w.title,
                category = "TRAINING (${w.durationMin} MIN • ${w.category.uppercase()})",
                categoryColor = NeonMint,
                description = w.notes,
                exercises = w.exercises
            )
        }

        // 3. Meals
        routine.meals.forEach { m ->
            TimelineCard(
                time = m.time,
                title = m.name,
                category = "NUTRITION • ${m.baseCalories} KCAL",
                categoryColor = ElectricCyan,
                description = m.notes.ifBlank { "Personalized macronutrient meal." },
                macroSummary = "P: ${m.proteinG.roundToInt()}g  |  C: ${m.carbsG.roundToInt()}g  |  F: ${m.fatG.roundToInt()}g"
            )
        }

        // 4. Reminders
        routine.reminders.forEach { r ->
            TimelineCard(
                time = r.time,
                title = "Habit Protocol",
                category = "RECOVERY & MINDSET",
                categoryColor = CyberViolet,
                description = r.message
            )
        }

        // 5. Sleep Event
        TimelineCard(
            time = routine.sleepTime,
            title = "Sleep & Deep Recovery",
            category = "SLEEP PROTOCOL",
            categoryColor = Color(0xFF5C6BC0),
            description = "Room cooled to 19°C. Total darkness. 8 hours of non-REM & REM cellular repair."
        )
    }
}

@Composable
fun TimelineCard(
    time: String,
    title: String,
    category: String,
    categoryColor: Color,
    description: String,
    macroSummary: String? = null,
    exercises: List<com.example.arisfitness.data.model.ExerciseDetail> = emptyList()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Time Column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(55.dp)
        ) {
            Text(
                text = time,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = categoryColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(categoryColor)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Content Card
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(DarkCardBorder)
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = category,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = categoryColor,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                if (macroSummary != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = macroSummary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonMint
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = TextMuted,
                    lineHeight = 16.sp
                )

                if (exercises.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkSurfaceVariant, RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "EXERCISE PROTOCOL:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        exercises.forEach { ex ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("• ${ex.name}", fontSize = 11.sp, color = TextWhite.copy(alpha = 0.9f))
                                Text("${ex.sets} × ${ex.reps}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonMint)
                            }
                        }
                    }
                }
            }
        }
    }
}
