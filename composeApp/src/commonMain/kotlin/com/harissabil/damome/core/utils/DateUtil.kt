package com.harissabil.damome.core.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

fun Instant.toReadableTime(): String {
    val tz = TimeZone.currentSystemDefault()
    val localDateTime = this.toLocalDateTime(tz)
    val time = localDateTime.time
    return time.format(LocalTime.Format {
        hour()
        char(':')
        minute()
    })
}

fun LocalDate.toYyyyMmDd(): String {
    return "${this.year}-${this.monthNumber}-${this.dayOfMonth}"
}

fun LocalTime.toHhMm(): String {
    return this.format(LocalTime.Format {
        hour()
        char(':')
        minute()
    })
}