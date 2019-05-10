package com.badoo.ribs.core.directory

import kotlin.reflect.KClass

interface Directory {

    val parent: Directory?

    fun <T : Any> getSubDirectory(key: KClass<T>) : Directory?

    fun <T : Any> getSubDirectoryOrSelf(key: KClass<T>) : Directory =
        getSubDirectory(key) ?: this

    fun <T : Any> get(key: KClass<T>) : T?

    fun <T : Any> getRecursively(key: KClass<T>) : T?

    fun <T : Any> getRecursivelyOrDefault(default: T) : T =
        get(default::class) ?: parent?.get(default::class) ?: default
}
