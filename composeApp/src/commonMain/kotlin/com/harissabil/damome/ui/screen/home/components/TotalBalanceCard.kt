package com.harissabil.damome.ui.screen.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.core.utils.Currency
import com.harissabil.damome.core.utils.formatCurrency
import com.harissabil.damome.ui.components.AutoSizeText
import com.harissabil.damome.ui.components.BaseCard
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun TotalBalanceCard(
    modifier: Modifier = Modifier,
    currency: Currency,
    value: Double,
) {
    BaseCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Balance",
                style = MiuixTheme.textStyles.headline2
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.large))
            AutoSizeText(
                text = "$currency ${formatCurrency(currency, value)}",
                modifier = Modifier.weight(1f),
                alignment = Alignment.CenterEnd,
                maxLines = 1,
                style = MiuixTheme.textStyles.title2,
                maxTextSize = MiuixTheme.textStyles.title2.fontSize,
            )
//            Text(
//                modifier = Modifier.weight(1f),
//                textAlign = TextAlign.End,
//                text = "$currency ${formatCurrency(currency, value)}",
//                style = MiuixTheme.textStyles.title2
//            )
        }
    }
}