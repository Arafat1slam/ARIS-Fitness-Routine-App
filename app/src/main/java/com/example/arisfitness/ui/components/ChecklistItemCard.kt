package com.example.arisfitness.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arisfitness.theme.DarkBackground
import com.example.arisfitness.theme.DarkCardBorder
import com.example.arisfitness.theme.DarkSurface
import com.example.arisfitness.theme.DarkSurfaceVariant
import com.example.arisfitness.theme.ElectricCyan
import com.example.arisfitness.theme.NeonMint
import com.example.arisfitness.theme.TextMuted
import com.example.arisfitness.theme.TextWhite
import com.example.arisfitness.util.SoundManager

@Composable
fun ChecklistItemCard(
    itemId: String,
    title: String,
    time: String,
    subtitle: String,
    categoryIcon: String,
    categoryColor: Color,
    isCompleted: Boolean,
    onToggle: (String) -> Unit,
    detailsText: String = "",
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val view = LocalView.current

    val cardBg by animateColorAsState(
        targetValue = if (isCompleted) Color(0xFF091216) else DarkSurface,
        label = "card_bg"
    )

    val checkScale by animateFloatAsState(
        targetValue = if (isCompleted) 1.0f else 0.88f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "check_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .bouncyClick(
                scaleDown = 0.98f,
                soundType = SoundType.CLICK
            ) {
                if (detailsText.isNotBlank()) isExpanded = !isExpanded
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCompleted) NeonMint.copy(alpha = 0.4f) else DarkCardBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Colored Vertical Indicator Bar
                Box(
                    modifier = Modifier
                        .size(width = 4.dp, height = 40.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (isCompleted) NeonMint else categoryColor)
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Category Avatar / Icon Box with subtle glowing container
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.radialGradient(
                                listOf(categoryColor.copy(alpha = 0.25f), categoryColor.copy(alpha = 0.08f))
                            )
                        )
                        .border(1.dp, categoryColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = categoryIcon,
                        fontSize = 22.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title, Time, Subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCompleted) TextMuted else TextWhite,
                            textDecoration = if (isCompleted) TextDecoration.LineThrough else null,
                            maxLines = 1
                        )

                        // Time Pill Tag
                        Box(
                            modifier = Modifier
                                .background(DarkSurfaceVariant, RoundedCornerShape(6.dp))
                                .border(0.5.dp, categoryColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = time,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCompleted) TextMuted else categoryColor
                            )
                        }
                    }

                    if (subtitle.isNotBlank()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = subtitle,
                            fontSize = 11.sp,
                            color = TextMuted,
                            maxLines = if (isExpanded) Int.MAX_VALUE else 1
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Reactive Custom Checkbox with sound trigger
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .scale(checkScale)
                        .clip(CircleShape)
                        .background(
                            if (isCompleted) NeonMint else DarkSurfaceVariant
                        )
                        .border(
                            width = 2.dp,
                            color = if (isCompleted) NeonMint else DarkCardBorder,
                            shape = CircleShape
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            val willBeCompleted = !isCompleted
                            if (willBeCompleted) {
                                SoundManager.playComplete(context)
                            } else {
                                SoundManager.playClick(context)
                            }
                            if (SoundManager.isHapticEnabled(context)) {
                                SoundManager.performHaptic(view)
                            }
                            onToggle(itemId)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Done",
                            tint = Color(0xFF00391A),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // Expandable details (notes, exercise list, instructions)
            AnimatedVisibility(
                visible = isExpanded && detailsText.isNotBlank(),
                enter = fadeIn() + expandVertically(spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkBackground.copy(alpha = 0.8f))
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = ElectricCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "PROTOCOL INSTRUCTIONS & DETAILS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan,
                            letterSpacing = 0.8.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = detailsText,
                        fontSize = 12.sp,
                        color = TextWhite.copy(alpha = 0.9f),
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}
