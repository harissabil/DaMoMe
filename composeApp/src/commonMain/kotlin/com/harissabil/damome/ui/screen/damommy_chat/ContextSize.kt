package com.harissabil.damome.ui.screen.damommy_chat

enum class ContextSize(val display: String, val description: String, val value: Int) {
    SMALL(
        display = "Small (Specific Answers)",
        description = "Best for short, precise responses.",
        value = 5
    ),
    MEDIUM(
        display = "Medium (Balanced Context)",
        description = "Includes some additional details for clarity.",
        value = 10
    ),
    LARGE(
        display = "Large (Broad Context)",
        description = "Covers multiple related topics for in-depth questions.",
        value = 15
    ),
}