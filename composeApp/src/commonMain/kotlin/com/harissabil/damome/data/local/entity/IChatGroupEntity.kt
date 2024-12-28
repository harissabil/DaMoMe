package com.harissabil.damome.data.local.entity

import kotlinx.datetime.Instant

interface IChatGroupEntity {
    var id: Long?
    var name: String
    var timestamp: Instant
}