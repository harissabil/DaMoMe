package com.harissabil.damome.data.local.object_box

import io.objectbox.Box
import kotlin.reflect.KClass

class CurrencyLocalStoreAndroid<T : Any>(
    entityClass: KClass<T>,
) : CurrencyLocalStore<T> {
    private val entityBox: Box<T> = ObjectBox.store.boxFor(entityClass.java)

    override fun <E> put(entity: CurrencyObject<E>) {
        val androidEntity = entity as CurrencyObjectAndroid<T>
        entityBox.put(androidEntity.entity)
    }

    override fun get(): T {
        return entityBox.all.first()
    }
}