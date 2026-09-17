package com.example.arisfitness.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.arisfitness.engine.MissedDayStatus
import com.example.arisfitness.theme.DarkCardBorder
import com.example.arisfitness.theme.DarkSurface
import com.example.arisfitness.theme.DarkSurfaceVariant
import com.example.arisfitness.theme.NeonMint
import com.example.arisfitness.theme.SolarAmber
import com.example.arisfitness.theme.TextMuted
import com.example.arisfitness.theme.TextWhite

@Composable
fun MissedDaysDialog(
    status: MissedDayStatus,
    onResumeFromSaved: () -> Unit,
    onCatchUpToCalendar: () -> Unit
) {
    Dialog(onDismissRequest = {}) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(SolarAmber)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(SolarAmber.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⏳", fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "MISSED DAYS DETECTED",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = SolarAmber,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "You were away for ${status.missedCount} calendar day(s). According to the ARIS protocol, routines never silently skip your training.",
                    fontSize = 13.sp,
                    color = TextMuted,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Comparison Box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurfaceVariant, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Saved Day", fontSize = 11.sp, color = TextMuted)
                        Text("Day ${status.currentSavedDayIndex}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NeonMint)
                    }
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(DarkCardBorder))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Calendar Day", fontSize = 11.sp, color = TextMuted)
                        Text("Day ${status.expectedCalendarDayIndex}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SolarAmber)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onResumeFromSaved,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonMint)
                ) {
                    Text(
                        text = "Continue From Day ${status.currentSavedDayIndex} (Pause Missed)",
                        color = Color(0xFF00391A),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onCatchUpToCalendar,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                ) {
                    Text(
                        text = "Catch Up to Day ${status.expectedCalendarDayIndex}",
                        color = TextWhite,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
