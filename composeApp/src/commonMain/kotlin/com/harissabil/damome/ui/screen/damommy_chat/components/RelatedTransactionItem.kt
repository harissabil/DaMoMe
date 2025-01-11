package com.harissabil.damome.ui.screen.damommy_chat.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.harissabil.damome.core.theme.errorDark
import com.harissabil.damome.core.theme.errorLight
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.core.utils.formatCurrency
import com.harissabil.damome.core.utils.toReadableDateTime
import com.harissabil.damome.domain.model.Category.Companion.toCategory
import com.harissabil.damome.domain.model.Transaction
import com.harissabil.damome.domain.model.TransactionType
import com.harissabil.damome.ui.components.BaseCard
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun RelatedTransactionItem(
    modifier: Modifier = Modifier,
    transaction: Transaction,
) {
    val category = transaction.category.toCategory()

    val prefix = if (transaction.type == TransactionType.INCOME) "+" else "-"

    BaseCard {
        Row(
            modifier = modifier.fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(MaterialTheme.spacing.medium),
        ) {
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = category.icon, contentDescription = null,
                        tint = category.color,
                    )
                    Text(
                        text = category.display,
                        style = MiuixTheme.textStyles.subtitle,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = transaction.description ?: "-",
                    style = MiuixTheme.textStyles.subtitle,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = transaction.timestamp.toReadableDateTime(),
                    style = MiuixTheme.textStyles.footnote1,
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    textAlign = TextAlign.End,
                    text = "$prefix${transaction.currency} ${
                        formatCurrency(
                            transaction.currency,
                            transaction.amount
                        )
                    }",
                    color = if (transaction.type == TransactionType.INCOME) MiuixTheme.colorScheme.primary else
                        if (isSystemInDarkTheme()) errorDark else errorLight,
                    style = MiuixTheme.textStyles.subtitle
                )
            }

        }
    }
}