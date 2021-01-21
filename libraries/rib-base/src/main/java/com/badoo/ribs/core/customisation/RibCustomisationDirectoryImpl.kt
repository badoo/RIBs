package com.badoo.ribs.core.customisation

import com.badoo.ribs.core.Rib
import kotlin.reflect.KClass

open class RibCustomisationDirectoryImpl(
    override val parent: RibCustomisationDirectory? = null
) : MutableRibCustomisationDirectory {

    private val map: MutableMap<Any, Any> = hashMapOf()

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

    override fun <T : Rib> getSubDirectoryOrSelf(key: KClass<T>): RibCustomisationDirectory {
        val subDir = map.keys.firstOrNull {
            it is KClass<*> && it.java.isAssignableFrom(key.java)
        }

        return map[subDir] as? RibCustomisationDirectory ?: this
    }

    operator fun KClass<out Rib>.invoke(block: RibCustomisationDirectoryImpl.() -> Unit) {
        if (map.containsKey(this)) {
            // TODO warning for accidental override?
        }
        map[this] = RibCustomisationDirectoryImpl(this@RibCustomisationDirectoryImpl).apply(block)
    }
}
