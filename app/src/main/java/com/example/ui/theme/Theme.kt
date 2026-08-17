package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val LifeCareColorScheme = lightColorScheme(
    primary = LifeCareTeal,
    onPrimary = Color.White,
    primaryContainer = LifeCareTealLight,
    onPrimaryContainer = LifeCareTealDark,
    secondary = LifeCarePeach,
    onSecondary = Color.White,
    secondaryContainer = LifeCarePeachLight,
    onSecondaryContainer = LifeCarePeachDark,
    tertiary = LifeCareEmergency,
    onTertiary = Color.White,
    tertiaryContainer = LifeCareEmergencyLight,
    onTertiaryContainer = LifeCareEmergency,
    background = LifeCareBackground,
    onBackground = LifeCareTextPrimary,
    surface = LifeCareSurface,
    onSurface = LifeCareTextPrimary,
    surfaceVariant = LifeCareSurfaceVariant,
    onSurfaceVariant = LifeCareTextSecondary,
    outline = LifeCareBorder,
    outlineVariant = LifeCareTextMuted
)

val LifeCareShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun LifeCareTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LifeCareColorScheme,
        typography = Typography,
        shapes = LifeCareShapes,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    LifeCareTheme(content = content)
}

