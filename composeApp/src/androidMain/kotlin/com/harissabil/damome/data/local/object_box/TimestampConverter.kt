package com.harissabil.damome.data.local.object_box

import io.objectbox.converter.PropertyConverter
import kotlinx.datetime.Instant

class TimestampConverter : PropertyConverter<Instant?, Long?> {
    override fun convertToDatabaseValue(entityProperty: Instant?): Long? {
        if (entityProperty == null) {
            return null
        }
        return entityProperty.toEpochMilliseconds()
    }

    override fun convertToEntityProperty(databaseValue: Long?): Instant? {
        if (databaseValue == null) {
            return null
        }
        return Instant.fromEpochMilliseconds(databaseValue)
    }
}