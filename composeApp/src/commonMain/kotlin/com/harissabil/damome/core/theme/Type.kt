package com.harissabil.damome.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import damome.composeapp.generated.resources.Res
import damome.composeapp.generated.resources.pbold
import damome.composeapp.generated.resources.pbold_italic
import damome.composeapp.generated.resources.pitalic
import damome.composeapp.generated.resources.plight
import damome.composeapp.generated.resources.pmedium
import damome.composeapp.generated.resources.pmedium_italic
import damome.composeapp.generated.resources.pregular
import damome.composeapp.generated.resources.psemibold
import damome.composeapp.generated.resources.psemibold_italic

@Composable
fun Typography(): Typography {
    val poppins = FontFamily(
        org.jetbrains.compose.resources.Font(Res.font.pregular, FontWeight.Normal, FontStyle.Normal),
        org.jetbrains.compose.resources.Font(Res.font.pitalic, FontWeight.Normal, FontStyle.Italic),
        org.jetbrains.compose.resources.Font(Res.font.pmedium, FontWeight.Medium, FontStyle.Normal),
        org.jetbrains.compose.resources.Font(Res.font.pmedium_italic, FontWeight.Medium, FontStyle.Italic),
        org.jetbrains.compose.resources.Font(Res.font.psemibold, FontWeight.SemiBold, FontStyle.Normal),
        org.jetbrains.compose.resources.Font(Res.font.psemibold_italic, FontWeight.SemiBold, FontStyle.Italic),
        org.jetbrains.compose.resources.Font(Res.font.pbold, FontWeight.Bold, FontStyle.Normal),
        org.jetbrains.compose.resources.Font(Res.font.pbold_italic, FontWeight.Bold, FontStyle.Italic),
        org.jetbrains.compose.resources.Font(Res.font.plight, FontWeight.Light, FontStyle.Normal),
    )

    return Typography(
        displayLarge = Typography().displayLarge.copy(fontFamily = poppins),
        displayMedium = Typography().displayMedium.copy(fontFamily = poppins),
        displaySmall = Typography().displaySmall.copy(fontFamily = poppins),

        headlineLarge = Typography().headlineLarge.copy(fontFamily = poppins),
        headlineMedium = Typography().headlineMedium.copy(fontFamily = poppins),
        headlineSmall = Typography().headlineSmall.copy(fontFamily = poppins),

        titleLarge = Typography().titleLarge.copy(fontFamily = poppins),
        titleMedium = Typography().titleMedium.copy(fontFamily = poppins),
        titleSmall = Typography().titleSmall.copy(fontFamily = poppins),

        bodyLarge = Typography().bodyLarge.copy(fontFamily = poppins),
        bodyMedium = Typography().bodyMedium.copy(fontFamily = poppins),
        bodySmall = Typography().bodySmall.copy(fontFamily = poppins),

        labelLarge = Typography().labelLarge.copy(fontFamily = poppins),
        labelMedium = Typography().labelMedium.copy(fontFamily = poppins),
        labelSmall = Typography().labelSmall.copy(fontFamily = poppins)
    )
}