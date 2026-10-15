package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = ElectricViolet,
  onPrimary = Color.White,
  primaryContainer = ElectricVioletDark,
  onPrimaryContainer = Color.White,
  secondary = CyberCyan,
  onSecondary = Color.Black,
  tertiary = CyberPink,
  background = ObsidianBg,
  onBackground = TextPrimary,
  surface = ObsidianSurface,
  onSurface = TextPrimary,
  surfaceVariant = ObsidianSurfaceElevated,
  onSurfaceVariant = TextSecondary,
  outline = ObsidianBorder,
  outlineVariant = Color(0xFF333E54)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep consistent high-end editorial dark palette
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content
  )
}
