package com.example.arisfitness.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arisfitness.data.model.CharacterProfile
import com.example.arisfitness.data.model.UserProfile
import com.example.arisfitness.theme.DarkBackground
import com.example.arisfitness.theme.DarkCardBorder
import com.example.arisfitness.theme.DarkSurface
import com.example.arisfitness.theme.DarkSurfaceVariant
import com.example.arisfitness.theme.ElectricCyan
import com.example.arisfitness.theme.FlameRed
import com.example.arisfitness.theme.NeonMint
import com.example.arisfitness.theme.SolarAmber
import com.example.arisfitness.theme.TextMuted
import com.example.arisfitness.theme.TextWhite
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    userProfile: UserProfile,
    onNavigateChangeCharacter: () -> Unit,
    onNavigateEditBiometrics: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var showChangeCharacterDialog by remember { mutableStateOf(false) }

    val character = CharacterProfile.getById(userProfile.selectedCharacterId)
    val accentColor = Color(character.accentColorHex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 18.dp)
            .verticalScroll(rememberScrollState())
            .padding(top = 28.dp, bottom = 48.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
            }
            Text(
                text = "SETTINGS & PROTOCOL",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                letterSpacing = 1.sp
            )
            Box(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Active Character Card
        Text("ACTIVE PROTOCOL", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextMuted, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkCardBorder))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.2f))
                            .border(1.5.dp, accentColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⚡", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = character.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "\"${character.alias}\" • ${character.intensityTag}",
                            fontSize = 12.sp,
                            color = accentColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { showChangeCharacterDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = SolarAmber, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Change Character Protocol", fontSize = 13.sp, color = TextWhite)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Biometrics & Metabolism
        Text("METABOLIC PROFILE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextMuted, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkCardBorder))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Height: ${userProfile.heightCm.roundToInt()} cm", fontSize = 13.sp, color = TextWhite)
                        Text("Weight: ${userProfile.weightKg} kg", fontSize = 13.sp, color = TextWhite)
                        Text("Age: ${userProfile.age} yrs", fontSize = 13.sp, color = TextWhite)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("BMR: ${userProfile.bmr.roundToInt()} kcal", fontSize = 13.sp, color = ElectricCyan, fontWeight = FontWeight.Bold)
                        Text("TDEE: ${userProfile.tdee.roundToInt()} kcal", fontSize = 13.sp, color = NeonMint, fontWeight = FontWeight.Bold)
                        Text("Activity: ${userProfile.activityLevel.replace('_', ' ').replaceFirstChar { it.uppercase() }}", fontSize = 11.sp, color = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onNavigateEditBiometrics,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Recalculate Biometrics & TDEE", fontSize = 13.sp, color = TextWhite)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Alarms & System Permissions
        Text("NOTIFICATIONS & ALARMS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextMuted, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(DarkCardBorder))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "System Channels Configured:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text("• Wake Alarms (High Priority Sound & Vibrate)", fontSize = 12.sp, color = TextMuted)
                Text("• Meal & Nutrition Reminders (High Priority)", fontSize = 12.sp, color = TextMuted)
                Text("• Workout Start Alarms (High Priority)", fontSize = 12.sp, color = TextMuted)
                Text("• Evening Sleep & Routine Reminders", fontSize = 12.sp, color = TextMuted)

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = NeonMint, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Manage Android Notification Permissions", fontSize = 12.sp, color = TextWhite)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // App Branding & Version
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ARIS FITNESS ROUTINE ENGINE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text("Version 1.0 (Build 2026) • 1,095-Day Protocol", fontSize = 11.sp, color = TextMuted)
            Text("Offline-first • Encrypted local Room SQLite storage", fontSize = 10.sp, color = TextMuted)
        }
    }

    // Change Character Confirmation Dialog (Spec Section 8 warning: resets day_index)
    if (showChangeCharacterDialog) {
        AlertDialog(
            onDismissRequest = { showChangeCharacterDialog = false },
            containerColor = DarkSurface,
            title = {
                Text("Switch Character Protocol?", color = FlameRed, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Text(
                    "Warning: In accordance with the ARIS Specification, changing your character protocol will reset your current progress to Day 1 of the new character's 1,095-day cycle. Are you sure you want to proceed?",
                    color = TextWhite.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showChangeCharacterDialog = false
                        onNavigateChangeCharacter()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FlameRed)
                ) {
                    Text("Proceed & Change", color = TextWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangeCharacterDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}
