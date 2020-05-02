package com.badoo.ribs.customisation

import com.badoo.ribs.core.Rib
import kotlin.reflect.KClass

open class RibCustomisationDirectoryImpl(
    override val parent: RibCustomisationDirectory? = null
) : MutableRibCustomisationDirectory {

    private val map: MutableMap<Any, Any> = hashMapOf()

    inline fun <reified T : Any> put(value: T) {
        put(T::class, value)
    }

    override fun <T : RibCustomisation> put(key: KClass<T>, value: T) {
        map[key] = value
    }

    fun <T : Any> put(vararg values: T) {
        values.forEach {
            map[it::class] = it
        }
    }

    inline operator fun <reified T : Any> T.unaryPlus() {
        put(this)
    }

    override fun <T : RibCustomisation> get(key: KClass<T>): T? =
        map[key] as? T

    override fun <T : RibCustomisation> getRecursively(key: KClass<T>): T? =
       get(key) ?: parent?.get(key)

    override fun <T : Rib> putSubDirectory(key: KClass<T>, value: RibCustomisationDirectory) {
        map[key] = value
    }

    override fun <T : Rib> getSubDirectory(key: KClass<T>): RibCustomisationDirectory?=
        map[key] as? RibCustomisationDirectory


    operator fun KClass<*>.invoke(block: RibCustomisationDirectoryImpl.() -> Unit) {
        if (map.containsKey(this)) {
            // TODO warning for accidental override?
        }
        map[this] = RibCustomisationDirectoryImpl(this@RibCustomisationDirectoryImpl).apply(block)
    }
}
