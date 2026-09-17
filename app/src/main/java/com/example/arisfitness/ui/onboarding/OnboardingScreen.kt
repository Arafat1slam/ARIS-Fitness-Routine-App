package com.example.arisfitness.ui.onboarding

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arisfitness.R
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
fun OnboardingScreen(
    onCompleteOnboarding: (heightCm: Float, weightKg: Float, age: Int, gender: String, activityLevel: String, bmr: Float, tdee: Float) -> Unit,
    initialHeight: Float = 175f,
    initialWeight: Float = 70f,
    initialAge: Int = 24,
    initialGender: String = "male",
    initialActivity: String = "moderate"
) {
    // Convert initial cm to feet & inches (175 cm ~ 5 ft 9 in)
    val initialTotalInches = (initialHeight / 2.54f).roundToInt()
    var heightFeet by remember { mutableIntStateOf((initialTotalInches / 12).coerceIn(4, 7)) }
    var heightInches by remember { mutableIntStateOf((initialTotalInches % 12).coerceIn(0, 11)) }

    var weightKg by remember { mutableFloatStateOf(initialWeight.coerceIn(35f, 160f)) }
    var age by remember { mutableIntStateOf(initialAge.coerceIn(16, 80)) }
    var gender by remember { mutableStateOf(initialGender) }
    var activityLevel by remember { mutableStateOf(initialActivity) }

    // Accurate cm calculation for Mifflin-St Jeor formula
    val totalInches = (heightFeet * 12) + heightInches
    val heightCm = totalInches * 2.54f

    val bmr = CalorieEngine.calculateBmr(weightKg, heightCm, age, gender)
    val tdee = CalorieEngine.calculateTdee(bmr, activityLevel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        // 1. Clean Header with Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_aris_logo),
                contentDescription = "ARIS Logo",
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "ARIS FITNESS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Personalized 3-Year Transformation",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title and description
        Text(
            text = "Set Up Your Profile",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
        Text(
            text = "Enter your body stats to calculate your exact daily calories and workout plan.",
            fontSize = 13.sp,
            color = TextMuted,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        // 2. Gender Selection (Male / Female)
        Text(
            text = "BIOLOGICAL GENDER",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextMuted,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("male" to "Male ♂", "female" to "Female ♀").forEach { (key, label) ->
                val isSelected = gender == key
                val bg = if (isSelected) NeonMint.copy(alpha = 0.15f) else DarkSurface
                val border = if (isSelected) NeonMint else DarkCardBorder
                val textColor = if (isSelected) NeonMint else TextWhite

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(bg)
                        .border(1.5.dp, border, RoundedCornerShape(14.dp))
                        .clickable { gender = key }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 15.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = textColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Height (Feet & Inches)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "HEIGHT (উচ্চতা)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$heightFeet ft $heightInches in",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonMint
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(${heightCm.roundToInt()} cm)",
                            fontSize = 12.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Feet Controls (4, 5, 6, 7 ft)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Feet:", fontSize = 13.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(4.dp))
                    listOf(4, 5, 6, 7).forEach { ft ->
                        val isSel = heightFeet == ft
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) NeonMint else DarkSurfaceVariant)
                                .clickable { heightFeet = ft }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$ft ft",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) Color(0xFF00391A) else TextWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Inches Slider (0 to 11 inches)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Inches:", fontSize = 13.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(10.dp))

                    // Minus button
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .clickable { if (heightInches > 0) heightInches -= 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease Inches", tint = TextWhite, modifier = Modifier.size(16.dp))
                    }

                    Slider(
                        value = heightInches.toFloat(),
                        onValueChange = { heightInches = it.roundToInt() },
                        valueRange = 0f..11f,
                        steps = 10,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMint,
                            activeTrackColor = NeonMint,
                            inactiveTrackColor = DarkSurfaceVariant
                        )
                    )

                    // Plus button
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .clickable { if (heightInches < 11) heightInches += 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase Inches", tint = TextWhite, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Weight (in KG)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "WEIGHT (ওজন)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                    Text(
                        text = "${weightKg.roundToInt()} kg",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonMint
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .clickable { if (weightKg > 35f) weightKg -= 1f },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease Weight", tint = TextWhite, modifier = Modifier.size(18.dp))
                    }

                    Slider(
                        value = weightKg,
                        onValueChange = { weightKg = it },
                        valueRange = 35f..150f,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMint,
                            activeTrackColor = NeonMint,
                            inactiveTrackColor = DarkSurfaceVariant
                        )
                    )

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .clickable { if (weightKg < 150f) weightKg += 1f },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase Weight", tint = TextWhite, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Age
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AGE (বয়স)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                    Text(
                        text = "$age years",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonMint
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .clickable { if (age > 16) age -= 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease Age", tint = TextWhite, modifier = Modifier.size(18.dp))
                    }

                    Slider(
                        value = age.toFloat(),
                        onValueChange = { age = it.roundToInt() },
                        valueRange = 16f..75f,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = NeonMint,
                            activeTrackColor = NeonMint,
                            inactiveTrackColor = DarkSurfaceVariant
                        )
                    )

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .clickable { if (age < 75) age += 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase Age", tint = TextWhite, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 6. Activity Level
        Text(
            text = "DAILY ACTIVITY LEVEL (প্রতিদিনের কাজ)",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextMuted,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        val activityOptions = listOf(
            Triple("sedentary", "Sedentary", "Desk job, little or no exercise"),
            Triple("light", "Lightly Active", "Exercise 1–3 days per week"),
            Triple("moderate", "Moderately Active", "Exercise 3–5 days per week (Recommended)"),
            Triple("active", "Very Active", "Intense workouts 6–7 days per week"),
            Triple("very_active", "Extremely Active", "Athletic training or physical labor")
        )

        activityOptions.forEach { (key, title, subtitle) ->
            val isSelected = activityLevel == key
            val bg = if (isSelected) NeonMint.copy(alpha = 0.12f) else DarkSurface
            val border = if (isSelected) NeonMint else DarkCardBorder

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { activityLevel = key },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = bg),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, border)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) NeonMint else TextWhite
                        )
                        Text(
                            text = subtitle,
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(NeonMint),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color(0xFF00391A),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 7. Clean Calorie Results Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "YOUR CALCULATED ENERGY TARGET",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("BMR (Rest)", fontSize = 12.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${bmr.roundToInt()} kcal",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Daily Target (TDEE)", fontSize = 12.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${tdee.roundToInt()} kcal",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonMint
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 8. Continue Button
        Button(
            onClick = {
                onCompleteOnboarding(heightCm, weightKg, age, gender, activityLevel, bmr, tdee)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonMint)
        ) {
            Text(
                text = "Next: Choose Workout Routine →",
                color = Color(0xFF00391A),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
