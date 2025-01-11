package com.harissabil.damome.data.local.object_box

import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.kotlin.toFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlin.reflect.KClass

class TransactionLocalStoreAndroid<T : Any>(
    entityClass: KClass<T>,
    private val typeProperty: Property<T>,
    private val timestampProperty: Property<T>,
    private val embeddingProperty: Property<T>,
) : TransactionLocalStore<T> {
    private val entityBox: Box<T> = ObjectBox.store.boxFor(entityClass.java)

    override suspend fun <E> put(entity: TransactionObject<E>) {
        val androidEntity = entity as TransactionObjectAndroid<T>
        entityBox.put(androidEntity.entity)
    }

    override suspend fun <E> delete(entity: TransactionObject<E>) {
        val androidEntity = entity as TransactionObjectAndroid<T>
        entityBox.remove(androidEntity.entity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findTransactionByDate(localDate: LocalDate): Flow<List<T>> {
        val tz = TimeZone.currentSystemDefault()
        val startInstant = localDate.atStartOfDayIn(tz)
        val endInstant = localDate.atTime(23, 59, 59).toInstant(tz)

        val query = entityBox.query(
            timestampProperty
                .greaterOrEqual(startInstant.toEpochMilliseconds())
                .and(
                    timestampProperty.less(endInstant.toEpochMilliseconds())
                )
        ).build()
        return query.subscribe().toFlow()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun all(): Flow<List<T>> {
        return entityBox.query().build().subscribe().toFlow()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun all(type: String): Flow<List<T>> {
        return entityBox.query(typeProperty.equal(type)).build().subscribe().toFlow()
    }

    override suspend fun findNearestNeighbors(
        queryVector: FloatArray,
        neighbors: Int,
        fromDate: LocalDate,
        toDate: LocalDate,
    ): List<T> {
        val tz = TimeZone.currentSystemDefault()
        val from = fromDate.atStartOfDayIn(tz).toEpochMilliseconds()
        val to = toDate.atTime(23, 59, 59).toInstant(tz).toEpochMilliseconds()

        val query = entityBox
            .query(embeddingProperty.nearestNeighbors(queryVector, neighbors))
            .between(timestampProperty, from, to)
            .build()
        return query.find()
    }
}