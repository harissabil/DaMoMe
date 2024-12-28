package com.harissabil.damome.data.local.object_box

import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.kotlin.toFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.reflect.KClass

class TransactionLocalStoreAndroid<T : Any>(
    entityClass: KClass<T>,
    private val timestampProperty: Property<T>,
    private val embeddingProperty: Property<T>,
) : TransactionLocalStore<T> {
    private val entityBox: Box<T> = ObjectBox.store.boxFor(entityClass.java)

    override fun <E> put(entity: TransactionObject<E>) {
        val androidEntity = entity as TransactionObjectAndroid<T>
        entityBox.put(androidEntity.entity)
    }

    override fun <E> delete(entity: TransactionObject<E>) {
        val androidEntity = entity as TransactionObjectAndroid<T>
        entityBox.remove(androidEntity.entity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun today(): Flow<List<T>> {
        val now = Clock.System.now()
        val tz = TimeZone.currentSystemDefault()
        val today = now.toLocalDateTime(tz).date

        val startInstant = today.atStartOfDayIn(tz)
        val endInstant = today.atTime(23, 59, 59).toInstant(tz)

        val query = entityBox.query(
            timestampProperty
                .greaterOrEqual(startInstant.toEpochMilliseconds())
                .and(
                    timestampProperty.less(endInstant.toEpochMilliseconds())
                )
        ).build()
        return query.subscribe().toFlow()
    }

    override fun all(): List<T> {
        return entityBox.all
    }

    override fun findNearestNeighbors(queryVector: FloatArray, neighbors: Int): List<T> {
        val query =
            entityBox.query(embeddingProperty.nearestNeighbors(queryVector, neighbors)).build()
        return query.find()
    }
}