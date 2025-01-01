package com.harissabil.damome.ui.components.speeddial_by_leinardi

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

/**
 * Enum that represents possible SpeedDial states.
 */
@Suppress("EnumNaming")
enum class SpeedDialState {
    /**
     * State that means a component is collapsed
     */
    Collapsed,

    /**
     * State that means a component is expanded
     */
    Expanded;

    fun toggle(): SpeedDialState = if (this == Expanded) Collapsed else Expanded
    fun isExpanded() = this == Expanded
}

/**
 * Return corresponding SpeedDialState based on a Boolean representation
 *
 * @param value whether the SpeedDialState is Expanded or Collapsed
 */

val LocalSpeedDialState: ProvidableCompositionLocal<MutableState<SpeedDialState>> = compositionLocalOf { error("No SpeedDialState provided") }