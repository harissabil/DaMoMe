package com.harissabil.damome.data.local.object_box

import com.harissabil.damome.core.utils.Currency
import io.objectbox.converter.PropertyConverter
import kotlin.collections.find

class CurrencyConverter : PropertyConverter<Currency, String> {
    override fun convertToDatabaseValue(entityProperty: Currency?): String {
        return entityProperty?.symbol ?: ""
    }

    override fun convertToEntityProperty(databaseValue: String?): Currency {
        return Currency.entries.find { it.symbol == databaseValue } ?: Currency.EMPTY
    }
}