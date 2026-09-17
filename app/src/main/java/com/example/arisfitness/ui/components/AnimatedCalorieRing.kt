package com.example.arisfitness.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import com.example.arisfitness.theme.DarkCardBorder
import com.example.arisfitness.theme.DarkSurface
import com.example.arisfitness.theme.ElectricCyan
import com.example.arisfitness.theme.NeonMint
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
        animationSpec = tween(durationMillis = 1000, delayMillis = 200, easing = FastOutSlowInEasing),
        label = "protein_anim"
    )
    val animCarbs by animateFloatAsState(
        targetValue = carbsProgress,
        animationSpec = tween(durationMillis = 1000, delayMillis = 300, easing = FastOutSlowInEasing),
        label = "carbs_anim"
    )
    val animFat by animateFloatAsState(
        targetValue = fatProgress,
        animationSpec = tween(durationMillis = 1000, delayMillis = 400, easing = FastOutSlowInEasing),
        label = "fat_anim"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(DarkCardBorder, DarkSurface)))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "NUTRITION PROTOCOL",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonMint,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "${((calorieProgress * 100).coerceAtMost(100f)).roundToInt()}% COMPLETE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rings and Center Display
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(190.dp)
            ) {
                Canvas(modifier = Modifier.size(180.dp)) {
                    val strokeWidth = 12.dp.toPx()
                    val center = Offset(size.width / 2, size.height / 2)

                    // Outer Ring: Calories
                    val radiusOuter = (size.width - strokeWidth) / 2
                    drawCircle(
                        color = Color(0xFF1C2433),
                        radius = radiusOuter,
                        center = center,
                        style = Stroke(width = strokeWidth)
                    )
                    drawArc(
                        brush = Brush.sweepGradient(listOf(NeonMint, ElectricCyan, NeonMint)),
                        startAngle = -90f,
                        sweepAngle = animCalorie * 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        topLeft = Offset(center.x - radiusOuter, center.y - radiusOuter),
                        size = Size(radiusOuter * 2, radiusOuter * 2)
                    )

                    // Middle Ring: Protein
                    val radiusMid = radiusOuter - strokeWidth - 5.dp.toPx()
                    val midStroke = 8.dp.toPx()
                    drawCircle(
                        color = Color(0xFF161E2C),
                        radius = radiusMid,
                        center = center,
                        style = Stroke(width = midStroke)
                    )
                    drawArc(
                        color = ColorProtein,
                        startAngle = -90f,
                        sweepAngle = animProtein * 360f,
                        useCenter = false,
                        style = Stroke(width = midStroke, cap = StrokeCap.Round),
                        topLeft = Offset(center.x - radiusMid, center.y - radiusMid),
                        size = Size(radiusMid * 2, radiusMid * 2)
                    )

                    // Inner Ring: Carbs
                    val radiusInner = radiusMid - midStroke - 4.dp.toPx()
                    val innerStroke = 7.dp.toPx()
                    drawCircle(
                        color = Color(0xFF131A26),
                        radius = radiusInner,
                        center = center,
                        style = Stroke(width = innerStroke)
                    )
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

                // Center Text Display
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$caloriesConsumed",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                    Text(
                        text = "/ $calorieTarget kcal",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Macro Breakdown Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MacroPill(
                    label = "Protein",
                    current = "${proteinConsumed.roundToInt()}g",
                    target = "${proteinTarget.roundToInt()}g",
                    color = ColorProtein
                )
                MacroPill(
                    label = "Carbs",
                    current = "${carbsConsumed.roundToInt()}g",
                    target = "${carbsTarget.roundToInt()}g",
                    color = ColorCarbs
                )
                MacroPill(
                    label = "Fats",
                    current = "${fatConsumed.roundToInt()}g",
                    target = "${fatTarget.roundToInt()}g",
                    color = ColorFat
                )
            }
        }
    }
}

@Composable
fun MacroPill(
    label: String,
    current: String,
    target: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = label, fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = current, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Text(text = "goal $target", fontSize = 10.sp, color = TextMuted)
    }
}
