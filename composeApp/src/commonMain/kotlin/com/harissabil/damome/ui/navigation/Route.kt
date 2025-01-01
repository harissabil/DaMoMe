package com.harissabil.damome.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    data object Onboarding : Route()

    @Serializable
    data object Home : Route()

    @Serializable
    data object Records : Route()

    @Serializable
    data object AskAi : Route()

    @Serializable
    data object DaMommy : Route()

    @Serializable
    data object More : Route()
}