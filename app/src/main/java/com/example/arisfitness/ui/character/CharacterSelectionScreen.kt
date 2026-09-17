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
import com.example.arisfitness.theme.ManaCyan
import com.example.arisfitness.theme.NeonMint
import com.example.arisfitness.theme.RadiantGold
import com.example.arisfitness.theme.ShadowPurple
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
            .padding(horizontal = 18.dp)
            .verticalScroll(rememberScrollState())
            .padding(top = 28.dp, bottom = 36.dp)
    ) {
        // Top HUD System Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(ShadowPurple.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                    .border(1.dp, ShadowPurple.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "SYSTEM // HERO AWAKENING ROSTER",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = ManaCyan,
                    letterSpacing = 1.5.sp
                )
            }
            Text(
                text = "5 SOVEREIGNS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = RadiantGold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "SELECT YOUR SOVEREIGN",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = TextWhite,
            letterSpacing = 1.sp
        )
        Text(
            text = "主人公選択 // 1,095-DAY HERO JOURNEY PROTOCOL",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ManaCyan,
            letterSpacing = 1.sp
        )
        Text(
            text = "Each archetype commands a unique training split, mindset philosophy, and baseline nutrition model.",
            fontSize = 12.sp,
            color = TextMuted,
            lineHeight = 17.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
        )

        // Character Cards
        characters.forEach { char ->
            val isSelected = char.characterId == selectedId
            val accentColor = Color(char.accentColorHex)
            val scaleFactor = CalorieEngine.calculateScaleFactor(userTdee, char.referenceTdee)
            val scaledCalories = (char.referenceTdee * scaleFactor).roundToInt()

            val rankTag = when (char.characterId) {
                "char_titan" -> "RANK S // TITAN"
                "char_ironclad" -> "RANK S // POWER"
                "char_aero" -> "RANK A // AGILITY"
                "char_shadow" -> "RANK A // SHADOW"
                "char_catalyst" -> "RANK B // VITALITY"
                else -> "RANK A"
            }

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
                shape = RoundedCornerShape(20.dp),
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
                        // Glowing Avatar
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(accentColor.copy(alpha = 0.2f))
                                .border(2.dp, accentColor, CircleShape),
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
                            Text(emoji, fontSize = 26.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = char.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "\"${char.alias}\"",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = accentColor
                                )
                            }
                            Text(
                                text = char.focus,
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }

                        // Rank Badge
                        Box(
                            modifier = Modifier
                                .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = rankTag,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = accentColor,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = char.description,
                        fontSize = 12.sp,
                        color = TextWhite.copy(alpha = 0.85f),
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

                    // Anime Caloric Mana Matrix
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkBackground.copy(alpha = 0.8f))
                            .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("REF BASELINE", fontSize = 9.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                            Text("${char.referenceTdee.toInt()} kcal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("SCALE RATIO", fontSize = 9.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                            Text("${(scaleFactor * 100).roundToInt()}%", fontSize = 13.sp, fontWeight = FontWeight.Black, color = accentColor)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("YOUR TARGET", fontSize = 9.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                            Text("$scaledCalories kcal", fontSize = 14.sp, fontWeight = FontWeight.Black, color = NeonMint)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val chosen = CharacterProfile.getById(selectedId)
        val chosenColor = Color(chosen.accentColorHex)

        Button(
            onClick = { onCharacterSelected(chosen) },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(chosenColor, ManaCyan)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "COMMIT TO ${chosen.alias.uppercase()} (1,095 DAYS) ⚡",
                        color = Color(0xFF050811),
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        letterSpacing = 1.2.sp
                    )
                }
            }
        }
    }
}
