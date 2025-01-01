package com.harissabil.damome.ui.components.speeddial_by_leinardi

import androidx.compose.foundation.lazy.LazyScopeMarker
import androidx.compose.runtime.Composable

/**
 * Receiver scope which is used by [SpeedDial].
 */
@LazyScopeMarker
class SpeedDialScope {
    private val _intervals = mutableListOf<SpeedDialIntervalContent>()
    internal val intervals: List<SpeedDialIntervalContent> = _intervals

    /**
     * Adds a single item.
     *
     * @param content the content of the item
     */
    fun item(
        content: @Composable SpeedDialItemScope.() -> Unit,
    ) {
        _intervals.add(0, SpeedDialIntervalContent(item = { content() }))
    }
}

internal data class SpeedDialIntervalContent(
    val item: @Composable SpeedDialItemScope.() -> Unit,
)