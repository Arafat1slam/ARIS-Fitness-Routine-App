package com.example.arisfitness.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arisfitness.theme.ColorCarbs
import com.example.arisfitness.theme.ColorFat
import com.example.arisfitness.theme.ColorProtein
import com.example.arisfitness.theme.DarkBackground
import com.example.arisfitness.theme.DarkCardBorder
import com.example.arisfitness.theme.DarkSurface
import com.example.arisfitness.theme.DarkSurfaceVariant
import com.example.arisfitness.theme.ElectricCyan
import com.example.arisfitness.theme.ManaCyan
import com.example.arisfitness.theme.NeonMagenta
import com.example.arisfitness.theme.NeonMint
import com.example.arisfitness.theme.RadiantGold
import com.example.arisfitness.theme.SolarAmber
import com.example.arisfitness.theme.TextMuted
import com.example.arisfitness.theme.TextWhite
import kotlin.math.roundToInt

@Composable
fun AnimatedCalorieRingCard(
    caloriesConsumed: Int,
    calorieTarget: Int,
    proteinConsumed: Float,
    proteinTarget: Float,
    carbsConsumed: Float,
    carbsTarget: Float,
    fatConsumed: Float,
    fatTarget: Float,
    modifier: Modifier = Modifier
) {
    val calorieProgress = if (calorieTarget > 0) (caloriesConsumed.toFloat() / calorieTarget).coerceIn(0f, 1.2f) else 0f
    val proteinProgress = if (proteinTarget > 0) (proteinConsumed / proteinTarget).coerceIn(0f, 1.2f) else 0f
    val carbsProgress = if (carbsTarget > 0) (carbsConsumed / carbsTarget).coerceIn(0f, 1.2f) else 0f
    val fatProgress = if (fatTarget > 0) (fatConsumed / fatTarget).coerceIn(0f, 1.2f) else 0f

    val animCalorie by animateFloatAsState(
        targetValue = calorieProgress,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "calorie_anim"
    )
    val animProtein by animateFloatAsState(
        targetValue = proteinProgress,
        animationSpec = tween(durationMillis = 1000, delayMillis = 150, easing = FastOutSlowInEasing),
        label = "protein_anim"
    )
    val animCarbs by animateFloatAsState(
        targetValue = carbsProgress,
        animationSpec = tween(durationMillis = 1000, delayMillis = 250, easing = FastOutSlowInEasing),
        label = "carbs_anim"
    )
    val animFat by animateFloatAsState(
        targetValue = fatProgress,
        animationSpec = tween(durationMillis = 1000, delayMillis = 350, easing = FastOutSlowInEasing),
        label = "fat_anim"
    )

    val completionPercent = ((calorieProgress * 100).coerceAtMost(100f)).roundToInt()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Brush.verticalGradient(
                listOf(
                    NeonMint.copy(alpha = 0.35f),
                    DarkCardBorder,
                    ElectricCyan.copy(alpha = 0.15f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NeonMint.copy(alpha = 0.05f),
                            Color.Transparent
                        ),
                        center = Offset(200f, 100f),
                        radius = 400f
                    )
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(NeonMint)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "NUTRITION PROTOCOL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        letterSpacing = 1.4.sp
                    )
                }

                // Dynamic Status Pill
                Box(
                    modifier = Modifier
                        .background(
                            if (completionPercent >= 100) NeonMint.copy(alpha = 0.2f) else DarkSurfaceVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .border(
                            1.dp,
                            if (completionPercent >= 100) NeonMint.copy(alpha = 0.6f) else DarkCardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$completionPercent% COMPLETE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = if (completionPercent >= 100) NeonMint else ElectricCyan,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Multi-Layered Neon Arc Gauges
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                Canvas(modifier = Modifier.size(190.dp)) {
                    val strokeWidth = 11.dp.toPx()
                    val center = Offset(size.width / 2, size.height / 2)

                    // Outer Gauge: Calories
                    val radiusOuter = (size.width - strokeWidth) / 2
                    // Background track with high-tech glow
                    drawCircle(
                        color = Color(0xFF141A28),
                        radius = radiusOuter,
                        center = center,
                        style = Stroke(width = strokeWidth)
                    )
                    // Subtly lit track accent
                    drawCircle(
                        color = NeonMint.copy(alpha = 0.12f),
                        radius = radiusOuter,
                        center = center,
                        style = Stroke(width = 2.dp.toPx())
                    )
                    if (animCalorie > 0f) {
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(NeonMint, ElectricCyan, NeonMint),
                                center = center
                            ),
                            startAngle = -90f,
                            sweepAngle = animCalorie * 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                            topLeft = Offset(center.x - radiusOuter, center.y - radiusOuter),
                            size = Size(radiusOuter * 2, radiusOuter * 2)
                        )
                    }

                    // Middle Gauge: Protein
                    val radiusMid = radiusOuter - strokeWidth - 6.dp.toPx()
                    val midStroke = 8.dp.toPx()
                    drawCircle(
                        color = Color(0xFF111722),
                        radius = radiusMid,
                        center = center,
                        style = Stroke(width = midStroke)
                    )
                    drawCircle(
                        color = ColorProtein.copy(alpha = 0.10f),
                        radius = radiusMid,
                        center = center,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                    if (animProtein > 0f) {
                        drawArc(
                            color = ColorProtein,
                            startAngle = -90f,
                            sweepAngle = animProtein * 360f,
                            useCenter = false,
                            style = Stroke(width = midStroke, cap = StrokeCap.Round),
                            topLeft = Offset(center.x - radiusMid, center.y - radiusMid),
                            size = Size(radiusMid * 2, radiusMid * 2)
                        )
                    }

                    // Inner Gauge: Carbs
                    val radiusInner = radiusMid - midStroke - 5.dp.toPx()
                    val innerStroke = 7.dp.toPx()
                    drawCircle(
                        color = Color(0xFF0F141E),
                        radius = radiusInner,
                        center = center,
                        style = Stroke(width = innerStroke)
                    )
                    drawCircle(
                        color = ColorCarbs.copy(alpha = 0.10f),
                        radius = radiusInner,
                        center = center,
                        style = Stroke(width = 1.dp.toPx())
                    )
                    if (animCarbs > 0f) {
                        drawArc(
                            color = ColorCarbs,
                            startAngle = -90f,
                            sweepAngle = animCarbs * 360f,
                            useCenter = false,
                            style = Stroke(width = innerStroke, cap = StrokeCap.Round),
                            topLeft = Offset(center.x - radiusInner, center.y - radiusInner),
                            size = Size(radiusInner * 2, radiusInner * 2)
                        )
                    }

                    // Innermost Gauge: Fats
                    val radiusFat = radiusInner - innerStroke - 4.dp.toPx()
                    val fatStroke = 6.dp.toPx()
                    drawCircle(
                        color = Color(0xFF0C1018),
                        radius = radiusFat,
                        center = center,
                        style = Stroke(width = fatStroke)
                    )
                    if (animFat > 0f) {
                        drawArc(
                            color = ColorFat,
                            startAngle = -90f,
                            sweepAngle = animFat * 360f,
                            useCenter = false,
                            style = Stroke(width = fatStroke, cap = StrokeCap.Round),
                            topLeft = Offset(center.x - radiusFat, center.y - radiusFat),
                            size = Size(radiusFat * 2, radiusFat * 2)
                        )
                    }
                }

                // Center Text Display (Futuristic HUD)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$caloriesConsumed",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "OF $calorieTarget KCAL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .background(DarkSurfaceVariant, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (caloriesConsumed >= calorieTarget && calorieTarget > 0) "TARGET REACHED" else "TARGET ACTIVE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = if (caloriesConsumed >= calorieTarget && calorieTarget > 0) NeonMint else ElectricCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Modern Interactive Macro Cards with Individual Progress Bars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModernMacroCard(
                    modifier = Modifier.weight(1f),
                    label = "Protein",
                    current = proteinConsumed.roundToInt(),
                    target = proteinTarget.roundToInt(),
                    color = ColorProtein
                )
                ModernMacroCard(
                    modifier = Modifier.weight(1f),
                    label = "Carbs",
                    current = carbsConsumed.roundToInt(),
                    target = carbsTarget.roundToInt(),
                    color = ColorCarbs
                )
                ModernMacroCard(
                    modifier = Modifier.weight(1f),
                    label = "Fats",
                    current = fatConsumed.roundToInt(),
                    target = fatTarget.roundToInt(),
                    color = ColorFat
                )
            }
        }
    }
}

@Composable
fun ModernMacroCard(
    modifier: Modifier = Modifier,
    label: String,
    current: Int,
    target: Int,
    color: Color
) {
    val progress = if (target > 0) (current.toFloat() / target).coerceIn(0f, 1f) else 0f
    val animProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "macro_progress"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurfaceVariant.copy(alpha = 0.7f))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
            .bouncyClick(scaleDown = 0.96f) {}
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                )
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${current}g",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "/${target}g",
                    fontSize = 10.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Mini Track Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(DarkBackground)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = animProgress)
                        .height(4.dp)
                        .background(color)
                )
            }
        }
    }
}
