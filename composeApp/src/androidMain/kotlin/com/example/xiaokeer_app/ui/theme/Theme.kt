package com.example.xiaokeer_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 微信绿色
val WeChatGreen = Color(0xFF3EB575)

// 统一背景色 - 浅灰色
private val UnifiedBackground = Color(0xFFF5F5F5)

// 现代简约风格的颜色定义 - 统一背景色
private val LightColors = lightColorScheme(
    primary = Color(0xFF2196F3),
    onPrimary = Color.White,
    primaryContainer = WeChatGreen,
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF607D8B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8E8E8),
    onSecondaryContainer = Color(0xFF455A64),
    background = UnifiedBackground,
    onBackground = Color(0xFF212121),
    surface = UnifiedBackground,
    onSurface = Color(0xFF212121),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF757575),
    outline = Color(0xFFBDBDBD),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF64B5F6),
    onPrimary = Color(0xFF0D47A1),
    primaryContainer = WeChatGreen,
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF90A4AE),
    onSecondary = Color(0xFF263238),
    secondaryContainer = Color(0xFF455A64),
    onSecondaryContainer = Color(0xFFCFD8DC),
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFF9E9E9E),
    outline = Color(0xFF616161),
)

@Composable
fun XiaokeerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
