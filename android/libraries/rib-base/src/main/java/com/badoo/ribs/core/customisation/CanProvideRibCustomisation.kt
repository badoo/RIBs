package com.badoo.ribs.core.customisation

import com.badoo.ribs.core.Rib
import kotlin.reflect.KClass

interface CanProvideRibCustomisation {
    fun ribCustomisation(): RibCustomisationDirectory
}

fun <T : Rib> CanProvideRibCustomisation.customisationsBranchFor(key: KClass<T>) : RibCustomisationDirectory =
    ribCustomisation().getSubDirectoryOrSelf(key)

fun <T : RibCustomisation> CanProvideRibCustomisation.getOrDefault(defaultCustomisation: T) : T =
    ribCustomisation().getRecursivelyOrDefault(defaultCustomisation)
