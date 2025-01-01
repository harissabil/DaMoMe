package com.harissabil.damome.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long? = 0,
    override var currency: String,
) : ICurrencyEntity