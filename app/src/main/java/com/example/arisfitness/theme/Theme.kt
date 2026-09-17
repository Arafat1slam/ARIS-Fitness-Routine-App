package com.example.arisfitness.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ArisDarkColorScheme = darkColorScheme(
    primary = NeonMint,
    onPrimary = Color(0xFF00391A),
    primaryContainer = Color(0xFF005228),
    onPrimaryContainer = NeonMint,

    secondary = ElectricCyan,
    onSecondary = Color(0xFF00363D),
    secondaryContainer = Color(0xFF004E59),
    onSecondaryContainer = ElectricCyan,

    tertiary = SolarAmber,
    onTertiary = Color(0xFF452200),
    tertiaryContainer = Color(0xFF663500),
    onTertiaryContainer = SolarAmber,

    background = DarkBackground,
    onBackground = TextWhite,

    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextMuted,

    outline = DarkCardBorder,
    outlineVariant = Color(0xFF1E2638)
)

@Composable
fun ARISFitnessTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ArisDarkColorScheme,
        typography = Typography,
        content = content
    )
}
