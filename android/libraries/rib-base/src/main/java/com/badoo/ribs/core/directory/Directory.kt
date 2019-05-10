package com.badoo.ribs.core.directory

import com.badoo.ribs.core.Rib
import kotlin.reflect.KClass

interface Directory {

    val parent: Directory?

    fun <T : Rib> getSubDirectory(key: KClass<T>) : Directory?

    fun <T : Rib> getSubDirectoryOrSelf(key: KClass<T>) : Directory =
        getSubDirectory(key) ?: this

    fun <T : RibCustomisation> get(key: KClass<T>) : T?

    fun <T : RibCustomisation> getRecursively(key: KClass<T>) : T?

    fun <T : RibCustomisation> getRecursivelyOrDefault(default: T) : T =
        get(default::class) ?: parent?.get(default::class) ?: default
}
