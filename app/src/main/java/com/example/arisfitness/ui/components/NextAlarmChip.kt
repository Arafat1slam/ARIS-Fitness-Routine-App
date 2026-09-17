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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arisfitness.theme.DarkCardBorder
import com.example.arisfitness.theme.DarkSurfaceVariant
import com.example.arisfitness.theme.ElectricCyan
import com.example.arisfitness.theme.NeonMint
import com.example.arisfitness.theme.TextMuted
import com.example.arisfitness.theme.TextWhite

@Composable
fun NextAlarmChip(
    nextEventTitle: String,
    nextEventTime: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_dot")
    val dotScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_scale"
    )

    Box(
        modifier = modifier
            .background(DarkSurfaceVariant, RoundedCornerShape(20.dp))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .scale(dotScale)
                    .background(NeonMint, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "NEXT PROTOCOL:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricCyan,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$nextEventTitle ($nextEventTime)",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextWhite
            )
        }
    }
}
