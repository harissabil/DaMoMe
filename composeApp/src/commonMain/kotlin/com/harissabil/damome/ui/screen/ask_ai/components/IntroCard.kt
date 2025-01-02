package com.harissabil.damome.ui.screen.ask_ai.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.ui.components.AutoSizeText
import damome.composeapp.generated.resources.Res
import damome.composeapp.generated.resources.img_damommy
import org.jetbrains.compose.resources.painterResource
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun IntroCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = MiuixTheme.colorScheme.disabledPrimary)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1.5f),
            ) {
                Text(
                    text = "DaMommy AI",
                    style = MiuixTheme.textStyles.title3.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Text(
                    text = "An AI that can help you manage your finances by analyzing your transaction data and providing insights based on it.",
                    style = MiuixTheme.textStyles.subtitle,
                )
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Image(
                modifier = Modifier.weight(1f),
                painter = painterResource(Res.drawable.img_damommy),
                contentDescription = "DaMommy AI",
            )
        }
    }
}