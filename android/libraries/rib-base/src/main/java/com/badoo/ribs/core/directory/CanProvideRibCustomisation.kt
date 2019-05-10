package com.badoo.ribs.core.directory

import com.badoo.ribs.core.Rib
import kotlin.reflect.KClass

interface CanProvideRibCustomisation {
    fun ribCustomisation(): Directory
}

fun <T : Rib> CanProvideRibCustomisation.customisationsBranchFor(key: KClass<T>) : Directory =
    ribCustomisation().getSubDirectoryOrSelf(key)

fun <T : Any> CanProvideRibCustomisation.getOrDefault(defaultCustomisation: T) : T =
    ribCustomisation().getRecursivelyOrDefault(defaultCustomisation)
