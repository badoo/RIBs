package com.badoo.ribs.core.customisation

import com.badoo.ribs.core.Rib
import kotlin.reflect.KClass

interface RibCustomisationDirectory {

    val parent: RibCustomisationDirectory?

    fun <T : Rib> getSubDirectory(key: KClass<T>) : RibCustomisationDirectory?

    fun <T : Rib> getSubDirectoryOrSelf(key: KClass<T>) : RibCustomisationDirectory =
        getSubDirectory(key) ?: this

    fun <T : RibCustomisation> get(key: KClass<T>) : T?

    fun <T : RibCustomisation> getRecursively(key: KClass<T>) : T?

    fun <T : RibCustomisation> getRecursivelyOrDefault(default: T) : T =
        get(default::class) ?: parent?.get(default::class) ?: default
}
