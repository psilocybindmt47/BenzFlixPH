package com.benzflix.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EightColor = Color(0xFFFFD700)
private val EightBg = Color(0xFF1A1A1A)

@Composable
fun BenzFlixTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = EightColor,
            background = EightBg,
            surface = EightBg,
            onPrimary = Color.Black,
            onBackground = EightColor
        ),
        typography = Typography(),
        content = content
    )
}