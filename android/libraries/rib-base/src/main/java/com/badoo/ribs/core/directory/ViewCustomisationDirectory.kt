package com.badoo.ribs.core.directory

import kotlin.reflect.KClass

open class ViewCustomisationDirectory(
    override val parent: Directory? = null
) : MutableDirectory {

    private val map: MutableMap<Any, Any> = hashMapOf()

    inline fun <reified T : Any> put(value: T) {
        put(T::class, value)
    }

    override fun <T : Any> put(key: KClass<T>, value: T) {
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

    override fun <T : Any> get(key: KClass<T>): T? =
        map[key] as? T

    override fun <T : Any> getRecursively(key: KClass<T>): T? =
       get(key) ?: parent?.get(key)

    override fun <T : Any> putSubDirectory(key: KClass<T>, value: Directory) {
        map[key] = value
    }

    override fun <T : Any> getSubDirectory(key: KClass<T>): Directory?=
        map[key] as? Directory


    operator fun KClass<*>.invoke(block: ViewCustomisationDirectory.() -> Unit) {
        if (map.containsKey(this)) {
            // TODO warning for accidental override?
        }
        map[this] = ViewCustomisationDirectory(this@ViewCustomisationDirectory).apply(block)
    }
}
