package com.badoo.ribs.core.directory

import com.badoo.ribs.core.Rib
import kotlin.reflect.KClass

interface MutableRibCustomisationDirectory : RibCustomisationDirectory {

    fun <T : Rib> putSubDirectory(key: KClass<T>, value: RibCustomisationDirectory)

    fun <T : RibCustomisation> put(key: KClass<T>, value: T)
}
