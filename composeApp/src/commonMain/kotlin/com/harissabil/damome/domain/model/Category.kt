package com.harissabil.damome.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.More
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class Category(
    val value: String,
    val display: String,
    val icon: ImageVector,
    val color: Color,
) {
    BILLS("bills", "Bills", Icons.Filled.Payments, Color(0xFFef9b20)),
    DEBTS("debts", "Debts", Icons.Filled.Paid, Color(0xFFf46a9b)),
    EDUCATION("education", "Education", Icons.Filled.School, Color(0xFFb33dc6)),
    ENTERTAINMENT("entertainment", "Entertainment", Icons.Filled.Celebration, Color(0xFFedbf33)),
    FAMILY("family", "Family", Icons.Filled.FamilyRestroom, Color(0xFF27aeef)),
    FOODS("foods", "Foods", Icons.Filled.Restaurant, Color(0xFFea5545)),
    HEALTH("health", "Health", Icons.Filled.MedicalServices, Color(0xFFbdcf32)),
    INCOME("income", "Income", Icons.Filled.MonetizationOn, Color(0xFF41B3A2)),
    SAVINGS("savings", "Savings", Icons.Filled.Savings, Color(0xFF87bc45)),
    SHOPPING("shopping", "Shopping", Icons.Filled.ShoppingCart, Color(0xFFede15b)),
    TRANSPORTATION(
        "transportation",
        "Transportation",
        Icons.Filled.DirectionsCar,
        Color(0xFFbfbfbf)
    ),
    EVENTS("events", "Events", Icons.Filled.Event, Color(0xFF0bb4ff)),
    TOP_UP("top_up", "Top up", Icons.Filled.AccountBalanceWallet, Color(0xFF8be04e)),
    OTHERS("others", "Others", Icons.AutoMirrored.Filled.More, Color(0xFFe14b31));

    companion object {
        fun String.toCategory(): Category {
            return entries.find { this == it.value } ?: OTHERS
        }
    }
}
