package com.lucasbueno.basiclogin.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = BlueDark,
    onPrimary = Color.White,
    secondary = OrangeDark,
    onSecondary = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
    surface = GreyDark,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    secondary = Orange,
    onSecondary = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    surface = GreyLight,
    onSurface = Color.Black
)

data class Spacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }

val MaterialTheme.padding: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current


@Composable
fun BasicLoginTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    CompositionLocalProvider(
        LocalSpacing provides Spacing()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}