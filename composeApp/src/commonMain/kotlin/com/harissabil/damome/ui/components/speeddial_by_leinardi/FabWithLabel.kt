package com.harissabil.damome.ui.components.speeddial_by_leinardi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FabWithLabel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    labelContent: @Composable (() -> Unit)? = null,
    labelBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    labelMaxWidth: Dp = 160.dp,
    labelContainerElevation: Dp = 2.dp,
    fabShape: Shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
    fabElevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    fabSize: Dp = SpeedDialMiniFabSize,
    fabBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    fabContentColor: Color = contentColorFor(fabBackgroundColor),
    fabContent: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(end = (SpeedDialFabSize - fabSize) / 2)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(
            (SpeedDialFabSize - fabSize) / 2 + 16.dp,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (labelContent != null) {
            Card(
                modifier = Modifier.widthIn(max = labelMaxWidth).clickable { onClick() },
                colors = CardDefaults.cardColors().copy(containerColor = labelBackgroundColor),
                elevation = CardDefaults.cardElevation(labelContainerElevation),
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    propagateMinConstraints = true,
                ) {
                    ProvideTextStyle(value = MaterialTheme.typography.bodyMedium) {
                        labelContent()
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(fabSize),
            shape = fabShape,
            containerColor = fabBackgroundColor,
            contentColor = fabContentColor,
            elevation = fabElevation,
        ) {
            fabContent()
        }
    }
}