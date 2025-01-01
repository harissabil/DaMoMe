package com.harissabil.damome.data.local.room

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilliseconds()
    }

    @TypeConverter
    fun fromFloatArray(value: String?): FloatArray? {
        return value?.split(",")?.map { it.toFloat() }?.toFloatArray()
    }

    @TypeConverter
    fun floatArrayToString(floatArray: FloatArray?): String? {
        return floatArray?.joinToString(",")
    }
}