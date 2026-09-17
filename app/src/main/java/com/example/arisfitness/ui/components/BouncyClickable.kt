package com.example.arisfitness.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.example.arisfitness.util.SoundManager

enum class SoundType {
    NONE,
    CLICK,
    COMPLETE
}

fun Modifier.bouncyClick(
    enabled: Boolean = true,
    scaleDown: Float = 0.94f,
    soundType: SoundType = SoundType.CLICK,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val context = LocalContext.current
    val view = LocalView.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) scaleDown else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "bouncy_scale"
    )

    this
        .scale(scale)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled
        ) {
            when (soundType) {
                SoundType.CLICK -> SoundManager.playClick(context)
                SoundType.COMPLETE -> SoundManager.playComplete(context)
                SoundType.NONE -> {}
            }
            if (SoundManager.isHapticEnabled(context)) {
                SoundManager.performHaptic(view)
            }
            onClick()
        }
}
