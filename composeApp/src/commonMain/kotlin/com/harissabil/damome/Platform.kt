package com.harissabil.damome

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform