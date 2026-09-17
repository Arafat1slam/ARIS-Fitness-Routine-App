package com.example.arisfitness.ui.progress

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arisfitness.data.model.UserProfile
import com.example.arisfitness.engine.DateResolutionEngine
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
fun ProgressScreen(
    userProfile: UserProfile,
    completedDaysCount: Int,
    onLogWeight: (Float) -> Unit,
    onBack: () -> Unit
) {
    var showWeightDialog by remember { mutableStateOf(false) }
    var weightInputText by remember { mutableStateOf("") }

    val elapsedDays = DateResolutionEngine.calculateElapsedDays(userProfile.startDate)
    val adherence = DateResolutionEngine.calculateAdherence(completedDaysCount, elapsedDays)
    val journeyProgress = (userProfile.currentDayIndex.toFloat() / 1095f).coerceIn(0f, 1f)

    val animatedAdherence by animateFloatAsState(
        targetValue = adherence / 100f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "adherence"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 18.dp)
            .verticalScroll(rememberScrollState())
            .padding(top = 28.dp, bottom = 48.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
            }
            Text(
                text = "3-YEAR PROTOCOL PROGRESS",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                letterSpacing = 1.sp
            )
            Box(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3-Year Progress Journey Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(NeonMint.copy(alpha = 0.4f), ElectricCyan.copy(alpha = 0.4f)))
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DAY ${userProfile.currentDayIndex}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonMint
                    )
                    Text(
                        text = "OF 1,095 DAYS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { journeyProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = NeonMint,
                    trackColor = DarkSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                val remainingDays = 1095 - userProfile.currentDayIndex
                Text(
                    text = "$remainingDays days remaining in this 3-year transformation.",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Adherence & Paused Days Summary Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Adherence Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkCardBorder))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ADHERENCE RATE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${adherence.roundToInt()}%",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = ElectricCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { animatedAdherence },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = ElectricCyan,
                        trackColor = DarkSurfaceVariant
                    )
                }
            }

            // Paused Days Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkCardBorder))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("PAUSED DAYS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${userProfile.pausedDays.size}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = SolarAmber
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Formal skips logged",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Weight Trend Chart Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkCardBorder))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("WEIGHT TREND", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextMuted, letterSpacing = 1.sp)
                        Text(
                            text = "${userProfile.weightKg} kg",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                    }
                    Button(
                        onClick = { showWeightDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonMint),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF00391A), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Log Weight", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00391A))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Canvas Line Chart for Weight
                val history = userProfile.weightHistory
                if (history.size >= 2) {
                    val weights = history.map { it.weightKg }
                    val minW = weights.minOrNull() ?: 50f
                    val maxW = (weights.maxOrNull() ?: 100f).coerceAtLeast(minW + 1f)

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    ) {
                        val width = size.width
                        val height = size.height
                        val stepX = width / (weights.size - 1)

                        val path = Path()
                        val points = mutableListOf<Offset>()

                        weights.forEachIndexed { idx, w ->
                            val normY = (w - minW) / (maxW - minW)
                            val px = idx * stepX
                            val py = height - (normY * (height - 30.dp.toPx())) - 15.dp.toPx()
                            points.add(Offset(px, py))
                            if (idx == 0) path.moveTo(px, py) else path.lineTo(px, py)
                        }

                        // Draw path
                        drawPath(
                            path = path,
                            brush = Brush.horizontalGradient(listOf(ElectricCyan, NeonMint)),
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Draw points
                        points.forEach { pt ->
                            drawCircle(color = NeonMint, radius = 4.dp.toPx(), center = pt)
                            drawCircle(color = DarkBackground, radius = 2.dp.toPx(), center = pt)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(DarkSurfaceVariant, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Log at least 2 weight entries to generate your visual trend chart.",
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3-Year Macrocycle Roadmap
        Text(
            text = "3-YEAR PHASE OVERVIEW",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = TextMuted,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        val phases = listOf(
            Triple("Phase I: Structural Foundation", "Days 1 – 90", "Neuromuscular adaptation & habit grounding"),
            Triple("Phase II: Progressive Overload", "Days 91 – 365", "Hypertrophy, density & progressive volume"),
            Triple("Phase III: High-Density Intensification", "Days 366 – 730", "Peak output, compound loading & conditioning"),
            Triple("Phase IV: Peak Elite Mastery", "Days 731 – 1,095", "Lifelong longevity, joint bulletproofing & mastery")
        )

        phases.forEachIndexed { index, (title, days, desc) ->
            val isCurrent = when (index) {
                0 -> userProfile.currentDayIndex <= 90
                1 -> userProfile.currentDayIndex in 91..365
                2 -> userProfile.currentDayIndex in 366..730
                else -> userProfile.currentDayIndex > 730
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = if (isCurrent) DarkSurfaceVariant else DarkSurface),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(if (isCurrent) NeonMint else DarkCardBorder)
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isCurrent) NeonMint else DarkCardBorder),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = if (isCurrent) Color(0xFF00391A) else TextWhite
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (isCurrent) NeonMint else TextWhite)
                            Text(days, fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
                        }
                        Text(desc, fontSize = 11.sp, color = TextMuted)
                    }
                }
            }
        }
    }

    // Weight Log Dialog
    if (showWeightDialog) {
        AlertDialog(
            onDismissRequest = { showWeightDialog = false },
            containerColor = DarkSurface,
            title = {
                Text("Log Current Weight", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column {
                    Text("Enter your current body weight in kg. This automatically recalculates your BMR and TDEE:", color = TextMuted, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = weightInputText,
                        onValueChange = { weightInputText = it },
                        placeholder = { Text("e.g. 72.5", color = TextMuted) },
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
                        val w = weightInputText.toFloatOrNull()
                        if (w != null && w in 30f..300f) {
                            onLogWeight(w)
                            showWeightDialog = false
                            weightInputText = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonMint)
                ) {
                    Text("Log Weight", color = Color(0xFF00391A), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWeightDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}
