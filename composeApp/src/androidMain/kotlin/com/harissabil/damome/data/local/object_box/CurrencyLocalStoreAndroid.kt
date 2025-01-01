package com.harissabil.damome.data.local.object_box

import io.objectbox.Box
import io.objectbox.kotlin.toFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

class CurrencyLocalStoreAndroid<T : Any>(
    entityClass: KClass<T>,
) : CurrencyLocalStore<T> {
    private val entityBox: Box<T> = ObjectBox.store.boxFor(entityClass.java)

    override suspend fun <E> put(entity: CurrencyObject<E>) {
        val androidEntity = entity as CurrencyObjectAndroid<T>
        entityBox.put(androidEntity.entity)
    }

    override suspend fun get(): T? {
        return entityBox.all.firstOrNull()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFlow(): Flow<List<T>> {
        return entityBox.query().build().subscribe().toFlow()
    }
}