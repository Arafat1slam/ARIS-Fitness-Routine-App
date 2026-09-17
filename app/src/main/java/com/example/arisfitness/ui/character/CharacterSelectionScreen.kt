package com.example.arisfitness.ui.character

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arisfitness.data.model.CharacterProfile
import com.example.arisfitness.engine.CalorieEngine
import com.example.arisfitness.theme.DarkBackground
import com.example.arisfitness.theme.DarkCardBorder
import com.example.arisfitness.theme.DarkSurface
import com.example.arisfitness.theme.DarkSurfaceVariant
import com.example.arisfitness.theme.NeonMint
import com.example.arisfitness.theme.TextMuted
import com.example.arisfitness.theme.TextWhite
import kotlin.math.roundToInt

@Composable
fun CharacterSelectionScreen(
    userTdee: Float,
    currentSelectedId: String = "char_titan",
    onCharacterSelected: (CharacterProfile) -> Unit
) {
    var selectedId by remember { mutableStateOf(currentSelectedId) }
    val characters = CharacterProfile.ALL_CHARACTERS

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .padding(top = 36.dp, bottom = 36.dp)
    ) {
        Text(
            text = "SELECT YOUR CHARACTER",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = NeonMint,
            letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Choose Your 3-Year Path",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
        Text(
            text = "Each character carries a distinct philosophy, day-indexed training split, and baseline nutrition model for all 1,095 days.",
            fontSize = 13.sp,
            color = TextMuted,
            lineHeight = 18.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        // Character Cards
        characters.forEach { char ->
            val isSelected = char.characterId == selectedId
            val accentColor = Color(char.accentColorHex)
            val scaleFactor = CalorieEngine.calculateScaleFactor(userTdee, char.referenceTdee)
            val scaledCalories = (char.referenceTdee * scaleFactor).roundToInt()

            val cardScale by animateFloatAsState(
                targetValue = if (isSelected) 1.02f else 1.0f,
                label = "scale_${char.characterId}"
            )
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) accentColor else DarkCardBorder,
                label = "border_${char.characterId}"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .scale(cardScale)
                    .clickable { selectedId = char.characterId },
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) DarkSurfaceVariant else DarkSurface
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(borderColor)
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Emoji Avatar
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(accentColor.copy(alpha = 0.2f))
                                .border(1.5.dp, accentColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            val emoji = when (char.characterId) {
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = char.name,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "\"${char.alias}\"",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = accentColor
                                )
                            }
                            Text(
                                text = char.focus,
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }

                        // Intensity Badge
                        Box(
                            modifier = Modifier
                                .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = char.intensityTag.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = accentColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = char.description,
                        fontSize = 12.sp,
                        color = TextWhite.copy(alpha = 0.8f),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "“${char.quote}”",
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        color = accentColor.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Calorie scaling info
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkBackground.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("BASE REFERENCE", fontSize = 10.sp, color = TextMuted)
                            Text("${char.referenceTdee.toInt()} kcal", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("SCALE RATIO", fontSize = 10.sp, color = TextMuted)
                            Text("${(scaleFactor * 100).roundToInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accentColor)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("YOUR TARGET", fontSize = 10.sp, color = TextMuted)
                            Text("$scaledCalories kcal", fontSize = 13.sp, fontWeight = FontWeight.Black, color = NeonMint)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val chosen = CharacterProfile.getById(selectedId)
        Button(
            onClick = { onCharacterSelected(chosen) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(chosen.accentColorHex))
        ) {
            Text(
                text = "Commit to ${chosen.alias}'s Protocol (1,095 Days) ⚡",
                color = Color(0xFF0D1117),
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )
        }
    }
}
