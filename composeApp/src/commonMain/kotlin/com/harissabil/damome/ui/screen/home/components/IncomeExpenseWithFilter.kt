package com.harissabil.damome.ui.screen.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.harissabil.damome.core.theme.errorDark
import com.harissabil.damome.core.theme.errorLight
import com.harissabil.damome.core.theme.spacing
import com.harissabil.damome.core.utils.Currency
import com.harissabil.damome.core.utils.formatCurrency
import com.harissabil.damome.ui.components.AutoSizeText
import com.harissabil.damome.ui.components.BaseCard
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun IncomeExpenseWithFilter(
    modifier: Modifier = Modifier,
    currency: Currency,
    totalIncome: Double,
    totalIncomeDaily: Double,
    totalIncomeWeekly: Double,
    totalIncomeMonthly: Double,
    totalIncomeYearly: Double,
    totalExpense: Double,
    totalExpenseDaily: Double,
    totalExpenseWeekly: Double,
    totalExpenseMonthly: Double,
    totalExpenseYearly: Double,
) {
    val incomeMap = mapOf(
        "Daily" to totalIncomeDaily,
        "Weekly" to totalIncomeWeekly,
        "Monthly" to totalIncomeMonthly,
        "Yearly" to totalIncomeYearly,
        "All Time" to totalIncome,
    )

    val expenseMap = mapOf(
        "Daily" to totalExpenseDaily,
        "Weekly" to totalExpenseWeekly,
        "Monthly" to totalExpenseMonthly,
        "Yearly" to totalExpenseYearly,
        "All Time" to totalExpense,
    )

    var selected by rememberSaveable { mutableStateOf("Daily") }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
                .padding(start = MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            incomeMap.forEach { (key, _) ->
                FilterChip(
                    label = {
                        Text(
                            text = key,
                            style = MiuixTheme.textStyles.headline2,
                            color = if (selected == key) MiuixTheme.colorScheme.onSurface else MiuixTheme.colorScheme.outline
                        )
                    },
                    selected = selected == key,
                    onClick = { selected = key },
                    colors = FilterChipDefaults.filterChipColors().copy(
                        containerColor = MiuixTheme.colorScheme.surface,
                        selectedContainerColor = MiuixTheme.colorScheme.surface,
                        selectedLabelColor = MiuixTheme.colorScheme.onSurface,
                    ),
                    shape = RoundedCornerShape(percent = 100),
                    border = BorderStroke(
                        width = 1.dp,
                        MiuixTheme.colorScheme.outline
                    )
                )
                if (key != incomeMap.keys.last()) {
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.small + MaterialTheme.spacing.extraSmall))
                } else {
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                }
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.spacing.medium),
        ) {
            val (incomeCard, expenseCard) = createRefs()

            IncomeExpenseCard(
                modifier = Modifier.constrainAs(incomeCard) {
                    start.linkTo(parent.start) // Tetap di sisi kiri
                    top.linkTo(parent.top)
                    end.linkTo(expenseCard.start, margin = 8.dp) // Beri jarak ke expenseCard
                    width =
                        Dimension.fillToConstraints // Batasi ukuran agar tidak melebihi constraints
                },
                icon = Icons.AutoMirrored.Filled.TrendingUp,
                title = "Income",
                currency = currency,
                amount = incomeMap[selected] ?: 0.0
            )

            IncomeExpenseCard(
                modifier = Modifier.constrainAs(expenseCard) {
                    start.linkTo(incomeCard.end, margin = 8.dp) // Beri jarak ke incomeCard
                    top.linkTo(parent.top)
                    end.linkTo(parent.end) // Tetap di sisi kanan
                    bottom.linkTo(incomeCard.bottom)
                    width =
                        Dimension.fillToConstraints // Batasi ukuran agar tidak melebihi constraints
                    height = Dimension.fillToConstraints // Sesuaikan ukuran dengan konten
                },
                icon = Icons.AutoMirrored.Filled.TrendingDown,
                title = "Expense",
                currency = currency,
                amount = expenseMap[selected] ?: 0.0
            )
        }
    }
}

@Composable
fun IncomeExpenseCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    currency: Currency,
    amount: Double,
) {
    BaseCard(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small + MaterialTheme.spacing.extraSmall)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Icon(
                    imageVector = icon, contentDescription = null,
                    tint = if (title == "Income") MiuixTheme.colorScheme.primary else if (isSystemInDarkTheme()) errorDark else errorLight
                )
                Text(text = title, style = MiuixTheme.textStyles.headline2)
            }

            val prefix = if (title == "Income") "+" else "-"

            AutoSizeText(
                text = "$prefix$currency ${formatCurrency(currency, amount)}",
                maxLines = 2,
                style = MiuixTheme.textStyles.title4,
                alignment = Alignment.TopStart,
                maxTextSize = MiuixTheme.textStyles.title4.fontSize,
            )
//            Text(
//                text = "$prefix$currency ${formatCurrency(currency, amount)}",
//                style = MiuixTheme.textStyles.title4
//            )
        }
    }
}