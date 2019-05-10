package com.badoo.ribs.core.directory

import com.badoo.ribs.core.Rib
import kotlin.reflect.KClass

interface MutableDirectory : Directory {

    fun <T : Rib> putSubDirectory(key: KClass<T>, value: Directory)

    fun <T : RibCustomisation> put(key: KClass<T>, value: T)
}
