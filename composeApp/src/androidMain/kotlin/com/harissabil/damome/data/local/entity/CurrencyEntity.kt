package com.harissabil.damome.data.local.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
data class CurrencyEntity(
    @Id(assignable = false)
    override var id: Long? = 0,
    @Unique
    override var currency: String,
) : ICurrencyEntity