package com.example.arisfitness.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arisfitness.theme.FlameRed
import com.example.arisfitness.theme.SolarAmber
import com.example.arisfitness.theme.TextWhite

@Composable
fun StreakFlameBadge(
    streakDays: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "flame_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFF2A1208), Color(0xFF1D0D06))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(FlameRed, SolarAmber)),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔥",
                fontSize = 16.sp,
                modifier = Modifier.scale(pulseScale)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$streakDays",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = SolarAmber
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (streakDays == 1) "DAY" else "DAYS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite.copy(alpha = 0.8f)
            )
        }
    }
}
