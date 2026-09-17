package com.example.arisfitness.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arisfitness.engine.CalorieEngine
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 32.dp)
    ) {
        // App Header & Branding
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(NeonMint, ElectricCyan))),
                contentAlignment = Alignment.Center
            ) {
                Text("⚡", fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "ARIS FITNESS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "3-YEAR ROUTINE PROTOCOL ENGINE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonMint,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Personalize Your Biometrics",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite
        )
        Text(
            text = "Caloric requirements are dynamically scaled via the Mifflin-St Jeor equation to power your selected character's 1,095-day routine.",
            fontSize = 13.sp,
            color = TextMuted,
            lineHeight = 18.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        // Live Calculated BMR & TDEE Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(NeonMint.copy(alpha = 0.5f), ElectricCyan.copy(alpha = 0.5f)))
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("ESTIMATED BMR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${bmr.roundToInt()} kcal",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = ElectricCyan
                    )
                }
                Box(modifier = Modifier.width(1.dp).height(40.dp).background(DarkCardBorder))
                Column(horizontalAlignment = Alignment.End) {
                    Text("DAILY TDEE TARGET", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonMint)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${tdee.roundToInt()} kcal",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonMint
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Gender Selection
        Text("GENDER", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("male" to "Male", "female" to "Female", "other" to "Other").forEach { (key, label) ->
                val isSelected = gender == key
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) NeonMint.copy(alpha = 0.15f) else DarkSurface)
                        .border(
                            1.dp,
                            if (isSelected) NeonMint else DarkCardBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { gender = key }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) NeonMint else TextMuted,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Height Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("HEIGHT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 1.sp)
            Text("${heightCm.roundToInt()} cm", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        }
        Slider(
            value = heightCm,
            onValueChange = { heightCm = it },
            valueRange = 130f..220f,
            colors = SliderDefaults.colors(
                thumbColor = NeonMint,
                activeTrackColor = NeonMint,
                inactiveTrackColor = DarkSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Weight Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("WEIGHT", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 1.sp)
            Text("${weightKg.roundToInt()} kg", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        }
        Slider(
            value = weightKg,
            onValueChange = { weightKg = it },
            valueRange = 40f..160f,
            colors = SliderDefaults.colors(
                thumbColor = ElectricCyan,
                activeTrackColor = ElectricCyan,
                inactiveTrackColor = DarkSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Age Slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("AGE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 1.sp)
            Text("$age years", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        }
        Slider(
            value = age.toFloat(),
            onValueChange = { age = it.roundToInt() },
            valueRange = 15f..80f,
            colors = SliderDefaults.colors(
                thumbColor = SolarAmber,
                activeTrackColor = SolarAmber,
                inactiveTrackColor = DarkSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Activity Level Selector
        Text("ACTIVITY LEVEL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))
        val activities = listOf(
            "sedentary" to ("Sedentary" to "Little or no exercise"),
            "light" to ("Light" to "1-3 days/week training"),
            "moderate" to ("Moderate" to "3-5 days/week training"),
            "active" to ("Active" to "6-7 days/week hard training"),
            "very_active" to ("Very Active" to "Physical job & intense training")
        )
        activities.forEach { (key, pair) ->
            val isSelected = activityLevel == key
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isSelected) DarkSurfaceVariant else DarkSurface)
                    .border(
                        1.dp,
                        if (isSelected) NeonMint else DarkCardBorder,
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { activityLevel = key }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = pair.first,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) NeonMint else TextWhite,
                            fontSize = 14.sp
                        )
                        Text(
                            text = pair.second,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                    if (isSelected) {
                        Text("✓", color = NeonMint, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                onCompleteOnboarding(heightCm, weightKg, age, gender, activityLevel, bmr, tdee)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonMint)
        ) {
            Text(
                text = "Choose Character Protocol →",
                color = Color(0xFF00391A),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
