package com.arm.composechat.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.arm.composechat.cache.AppThemeCache
import com.arm.composechat.model.AppTheme

private val LightColorPalette = lightColors(
    primary = PrimaryColorLight,
    primaryVariant = PrimaryVariantColorLight,
    secondary = SecondaryColorLight,
    secondaryVariant = SecondaryVariantColorLight,
    background = BackgroundColorLight,
    surface = SurfaceColorLight,
)

private val DarkColorPalette = darkColors(
    primary = PrimaryColorDark,
    primaryVariant = PrimaryVariantColorDark,
    secondary = SecondaryColorDark,
    secondaryVariant = SecondaryVariantColorDark,
    background = BackgroundColorDark,
    surface = SurfaceColorDark,
)

private val BlueColorPalette = lightColors(
    primary = PrimaryColorBlue,
    primaryVariant = PrimaryVariantColorBlue,
    secondary = SecondaryColorBlue,
    secondaryVariant = SecondaryVariantColorBlue,
    background = BackgroundColorBlue,
    surface = SurfaceColorBlue,
)

private val PinkColorPalette = lightColors(
    primary = PrimaryColorPink,
    primaryVariant = PrimaryVariantColorPink,
    secondary = SecondaryColorPink,
    secondaryVariant = SecondaryVariantColorPink,
    background = BackgroundColorPink,
    surface = SurfaceColorPink,
)

@Composable
fun ChatTheme(
    appTheme: AppTheme = AppThemeCache.currentTheme,
    content: @Composable () -> Unit
) {
    val colors = when (appTheme) {
        AppTheme.Light -> {
            LightColorPalette
        }
        AppTheme.Dark -> {
            DarkColorPalette
        }
        AppTheme.Blue -> {
            BlueColorPalette
        }
        AppTheme.Pink -> {
            PinkColorPalette
        }
    }
    val typography = when (appTheme) {
        AppTheme.Light -> {
            LightTypography
        }
        AppTheme.Dark -> {
            DarkTypography
        }
        AppTheme.Blue -> {
            BlueTypography
        }
        AppTheme.Pink -> {
            PinkTypography
        }
    }
    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = AppShapes,
        content = content
    )
}