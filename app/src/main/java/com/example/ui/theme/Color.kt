package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val SkyBluePrimary = Color(0xFF0288D1)
val SkyBlueContainer = Color(0xFFE1F5FE)
val OnSkyBlueContainer = Color(0xFF01579B)

val SunshineYellow = Color(0xFFFFC107)
val SunshineContainer = Color(0xFFFFF8E1)
val OnSunshineContainer = Color(0xFFF57F17)

val CoralPink = Color(0xFFFF4081)
val CoralContainer = Color(0xFFFCE4EC)
val OnCoralContainer = Color(0xFFC2185B)

val MintGreen = Color(0xFF4CAF50)
val MintContainer = Color(0xE8E8F5E9)
val OnMintContainer = Color(0xFF1B5E20)

val PlayfulPurple = Color(0xFF7E57C2)
val PurpleContainer = Color(0xFFEDE7F6)
val OnPurpleContainer = Color(0xFF4527A0)

val WarmBackground = Color.Transparent
val CardSurface = Color(0xFFFFFFFF)
val KidStarOrange = Color(0xFFFF9800)

val PlayfulBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFBAE6FD), // Sky Blue (top)
        Color(0xFFFEF08A), // Bright Yellow
        Color(0xFFFBCFE8), // Pink
        Color(0xFFBBF7D0), // Mint Green
        Color(0xFFDDD6FE)  // Lavender (bottom)
    )
)

