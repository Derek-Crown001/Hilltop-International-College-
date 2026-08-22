package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = HilltopAccent,
    onPrimary = Color.Black,
    primaryContainer = HilltopPrimaryDark,
    onPrimaryContainer = Color.White,
    secondary = HilltopSecondary,
    onSecondary = Color.White,
    tertiary = AcademicGold,
    background = HilltopBgDark,
    surface = HilltopSurfaceDark,
    onBackground = TextLight,
    onSurface = TextLight,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1)
)

private val LightColorScheme = lightColorScheme(
    primary = HilltopPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E4FF),
    onPrimaryContainer = HilltopPrimaryDark,
    secondary = HilltopSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0E8F9),
    onSecondaryContainer = HilltopPrimary,
    tertiary = HilltopAccent,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE0F7FA),
    onTertiaryContainer = HilltopAccentDark,
    background = AcademicBgLight,
    surface = CardWhite,
    onBackground = TextDark,
    onSurface = TextDark,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextMuted,
    outline = Color(0xFFCBD5E1)
)

@Composable
fun HilltopCollegeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our branding colors by default for consistent academic look
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
