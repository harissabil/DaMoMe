package com.harissabil.damome.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.darkColorScheme
import top.yukonga.miuix.kmp.theme.lightColorScheme

val lightScheme = androidx.compose.material3.lightColorScheme(
    primary = lightColorScheme().primary,
    onPrimary = lightColorScheme().onPrimary,
    primaryContainer = lightColorScheme().primaryContainer,
    onPrimaryContainer = lightColorScheme().onPrimaryContainer,
    secondary = lightColorScheme().secondary,
    onSecondary = lightColorScheme().onSecondary,
    secondaryContainer = lightColorScheme().secondaryContainer,
    onSecondaryContainer = lightColorScheme().onSecondaryContainer,
    tertiary = lightColorScheme().tertiaryContainer,
    onTertiary = lightColorScheme().onTertiaryContainer,
    tertiaryContainer = lightColorScheme().tertiaryContainer,
    onTertiaryContainer = lightColorScheme().onTertiaryContainer,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = lightColorScheme().background,
    onBackground = lightColorScheme().onBackground,
    surface = lightColorScheme().surface,
    onSurface = lightColorScheme().onSurface,
    surfaceVariant = lightColorScheme().surfaceVariant,
    onSurfaceVariant = lightColorScheme().onSurfaceVariantActions,
    outline = lightColorScheme().outline,
    outlineVariant = lightColorScheme().outline,
    surfaceContainer = lightColorScheme().surfaceContainer,
    surfaceContainerHigh = lightColorScheme().surfaceContainerHigh,
    surfaceContainerHighest = lightColorScheme().surfaceContainerHighest,
)

val darkScheme = androidx.compose.material3.darkColorScheme(
    primary = darkColorScheme().primary,
    onPrimary = darkColorScheme().onPrimary,
    primaryContainer = darkColorScheme().primaryContainer,
    onPrimaryContainer = darkColorScheme().onPrimaryContainer,
    secondary = darkColorScheme().secondary,
    onSecondary = darkColorScheme().onSecondary,
    secondaryContainer = darkColorScheme().secondaryContainer,
    onSecondaryContainer = darkColorScheme().onSecondaryContainer,
    tertiary = darkColorScheme().tertiaryContainer,
    onTertiary = darkColorScheme().onTertiaryContainer,
    tertiaryContainer = darkColorScheme().tertiaryContainer,
    onTertiaryContainer = darkColorScheme().onTertiaryContainer,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = darkColorScheme().background,
    onBackground = darkColorScheme().onBackground,
    surface = darkColorScheme().surface,
    onSurface = darkColorScheme().onSurface,
    surfaceVariant = darkColorScheme().surfaceVariant,
    onSurfaceVariant = darkColorScheme().onSurfaceVariantActions,
    outline = darkColorScheme().outline,
    outlineVariant = darkColorScheme().outline,
    surfaceContainer = darkColorScheme().surfaceContainer,
    surfaceContainerHigh = darkColorScheme().surfaceContainerHigh,
    surfaceContainerHighest = darkColorScheme().surfaceContainerHighest,
)

@Composable
fun DaMoMeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MiuixTheme(
        colors = if (darkTheme) darkColorScheme() else lightColorScheme(),
        textStyles = MyTextStyles(),
        content = content
    )
}