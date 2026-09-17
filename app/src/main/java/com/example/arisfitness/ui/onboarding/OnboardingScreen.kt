package com.example.arisfitness.ui.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arisfitness.R
import com.example.arisfitness.engine.CalorieEngine
import com.example.arisfitness.theme.DarkBackground
import com.example.arisfitness.theme.DarkCardBorder
import com.example.arisfitness.theme.DarkSurface
import com.example.arisfitness.theme.DarkSurfaceVariant
import com.example.arisfitness.theme.FlameRed
import com.example.arisfitness.theme.ManaCyan
import com.example.arisfitness.theme.NeonMagenta
import com.example.arisfitness.theme.NeonMint
import com.example.arisfitness.theme.RadiantGold
import com.example.arisfitness.theme.ShadowPurple
import com.example.arisfitness.theme.TextMuted
import com.example.arisfitness.theme.TextWhite
import kotlin.math.roundToInt

@Composable
fun OnboardingScreen(
    onCompleteOnboarding: (heightCm: Float, weightKg: Float, age: Int, gender: String, activityLevel: String, bmr: Float, tdee: Float) -> Unit,
    initialHeight: Float = 175f,
    initialWeight: Float = 72f,
    initialAge: Int = 24,
    initialGender: String = "male",
    initialActivity: String = "moderate"
) {
    var heightCm by remember { mutableFloatStateOf(initialHeight) }
    var weightKg by remember { mutableFloatStateOf(initialWeight) }
    var age by remember { mutableIntStateOf(initialAge) }
    var gender by remember { mutableStateOf(initialGender) }
    var activityLevel by remember { mutableStateOf(initialActivity) }

    val bmr = CalorieEngine.calculateBmr(weightKg, heightCm, age, gender)
    val tdee = CalorieEngine.calculateTdee(bmr, activityLevel)

    // Glowing aura animation
    val infiniteTransition = rememberInfiniteTransition(label = "anime_aura")
    val auraOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 28.dp)
    ) {
        // Top HUD System Notification
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(ShadowPurple.copy(alpha = 0.18f), RoundedCornerShape(6.dp))
                    .border(1.dp, ShadowPurple.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "SYSTEM // PLAYER AWAKENING",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = ManaCyan,
                    letterSpacing = 1.5.sp
                )
            }
            Text(
                text = "QUEST ID: #1095",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = RadiantGold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // App Logo & Header with Anime Styling
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(
                        width = 2.dp,
                        brush = Brush.sweepGradient(listOf(ManaCyan, ShadowPurple, NeonMagenta, ManaCyan)),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_aris_logo),
                    contentDescription = "ARIS Logo",
                    modifier = Modifier.size(46.dp).clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "ARIS",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PROTOCOLS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ManaCyan,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = "ステータス更新 // BIOMETRIC SYNCHRONIZATION",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = ShadowPurple,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Holographic Anime Status Window (BMR & TDEE)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF10172D), Color(0xFF0A0F1E))
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(
                        listOf(
                            ManaCyan.copy(alpha = 0.4f + auraOffset * 0.4f),
                            ShadowPurple.copy(alpha = 0.8f - auraOffset * 0.3f),
                            NeonMagenta.copy(alpha = 0.5f)
                        )
                    ),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "◆ SYSTEM STATUS CORE ◆",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = ManaCyan,
                        letterSpacing = 1.5.sp
                    )
                    Box(
                        modifier = Modifier
                            .background(RadiantGold.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .border(1.dp, RadiantGold, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "ACTIVE SYNC",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = RadiantGold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // BMR Core
                    Column {
                        Text(
                            text = "BASE METABOLIC RATE (BMR)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${bmr.roundToInt()}",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black,
                            color = ManaCyan
                        )
                        Text(
                            text = "KCAL / RECOVERY LEVEL",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = ManaCyan.copy(alpha = 0.7f)
                        )
                    }

                    // TDEE Core
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "COMBAT OUTPUT (TDEE)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ShadowPurple
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${tdee.roundToInt()}",
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Black,
                            color = NeonMint
                        )
                        Text(
                            text = "KCAL / DAILY TARGET",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonMint.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Class / Gender Selection
        Text(
            text = "CHOOSE AVATAR CLASS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = TextMuted,
            letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val classes = listOf(
                Triple("male", "SHADOW", "⚔️"),
                Triple("female", "VALKYRIE", "🗡️"),
                Triple("other", "RONIN", "🌌")
            )
            classes.forEach { (key, title, icon) ->
                val isSelected = gender == key
                val borderColor by animateColorAsState(
                    targetValue = if (isSelected) ManaCyan else DarkCardBorder,
                    label = "border_$key"
                )
                val bgBrush = if (isSelected) {
                    Brush.verticalGradient(listOf(Color(0xFF0F1E38), Color(0xFF0A1224)))
                } else {
                    Brush.verticalGradient(listOf(DarkSurface, DarkSurface))
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(bgBrush)
                        .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                        .clickable { gender = key }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(icon, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSelected) ManaCyan else TextMuted,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // STAT 1: HEIGHT / STATURE
        AnimeStatCard(
            title = "FRAME // STATURE",
            unit = "cm",
            currentValue = heightCm.roundToInt(),
            minValue = 130,
            maxValue = 220,
            accentColor = ManaCyan,
            icon = "⚡",
            onValueChange = { heightCm = it.toFloat() }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // STAT 2: WEIGHT / BODY MASS
        AnimeStatCard(
            title = "ARMOR // BODY MASS",
            unit = "kg",
            currentValue = weightKg.roundToInt(),
            minValue = 40,
            maxValue = 180,
            accentColor = ShadowPurple,
            icon = "🛡️",
            onValueChange = { weightKg = it.toFloat() }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // STAT 3: AGE / CHRONO CYCLES
        AnimeStatCard(
            title = "CHRONO // EXPERIENCE CYCLES",
            unit = "yrs",
            currentValue = age,
            minValue = 15,
            maxValue = 80,
            accentColor = RadiantGold,
            icon = "⏳",
            onValueChange = { age = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Activity Level / Guild Quest Ranks
        Text(
            text = "HUNTER GUILD QUEST INTENSITY",
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = TextMuted,
            letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        val rankActivities = listOf(
            Triple("sedentary", "RANK: E // NOVICE", "Sedentary • Low metabolic consumption"),
            Triple("light", "RANK: D // SCOUT", "1-3 days/week • Moderate stamina load"),
            Triple("moderate", "RANK: C // STRIKER", "3-5 days/week • Standard training regimen"),
            Triple("active", "RANK: B // VANGUARD", "6-7 days/week • High-octane physical demands"),
            Triple("very_active", "RANK: S // MONARCH", "Daily intense training • Peak energy throughput")
        )

        rankActivities.forEach { (key, rankTitle, desc) ->
            val isSelected = activityLevel == key
            val rankColor = when (key) {
                "very_active" -> RadiantGold
                "active" -> FlameRed
                "moderate" -> NeonMint
                "light" -> ManaCyan
                else -> TextMuted
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        brush = if (isSelected) {
                            Brush.horizontalGradient(listOf(Color(0xFF141930), Color(0xFF0F1224)))
                        } else {
                            androidx.compose.ui.graphics.SolidColor(DarkSurface)
                        }
                    )
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) rankColor else DarkCardBorder,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { activityLevel = key }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = rankTitle,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = if (isSelected) rankColor else TextWhite,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = desc,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(rankColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✓", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Epic Awaken Protocol CTA Button
        Button(
            onClick = {
                onCompleteOnboarding(heightCm, weightKg, age, gender, activityLevel, bmr, tdee)
            },
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
                            listOf(ShadowPurple, ManaCyan, NeonMint)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AWAKEN PROTOCOL // SELECT CHARACTER",
                        color = Color(0xFF050811),
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("⚡", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun AnimeStatCard(
    title: String,
    unit: String,
    currentValue: Int,
    minValue: Int,
    maxValue: Int,
    accentColor: Color,
    icon: String,
    onValueChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(accentColor.copy(alpha = 0.5f), DarkCardBorder)
            )
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(icon, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMuted,
                        letterSpacing = 1.2.sp
                    )
                }

                // Value Display with glowing accent
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$currentValue",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = accentColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = unit,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Controls: Minus Button, Slider, Plus Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tactical Minus Button
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(8.dp))
                        .clickable {
                            if (currentValue > minValue) onValueChange(currentValue - 1)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Smooth Neon Slider
                Slider(
                    value = currentValue.toFloat(),
                    onValueChange = { onValueChange(it.roundToInt()) },
                    valueRange = minValue.toFloat()..maxValue.toFloat(),
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = accentColor,
                        activeTrackColor = accentColor,
                        inactiveTrackColor = DarkSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Tactical Plus Button
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(8.dp))
                        .clickable {
                            if (currentValue < maxValue) onValueChange(currentValue + 1)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
