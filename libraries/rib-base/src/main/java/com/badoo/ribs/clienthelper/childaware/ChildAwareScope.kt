package com.badoo.ribs.clienthelper.childaware

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Rib

class ChildAwareScope(
    private val childAware: ChildAware,
    val lifecycle: Lifecycle,
) : ChildAware by childAware {

    inline fun <reified T : Rib> whenChildBuilt(
        noinline callback: ChildCallback<T>,
    ) {
        whenChildBuilt(lifecycle, callback)
    }

    inline fun <reified T : Rib> whenChildAttached(
        noinline callback: ChildCallback<T>,
    ) {
        whenChildAttached(lifecycle, callback)
    }

    inline fun <reified T1 : Rib, reified T2 : Rib> whenChildrenBuilt(
        noinline callback: ChildrenCallback<T1, T2>,
    ) {
        whenChildrenBuilt(lifecycle, callback)
    }

    inline fun <reified T1 : Rib, reified T2 : Rib> whenChildrenAttached(
        noinline callback: ChildrenCallback<T1, T2>,
    ) {
        whenChildrenAttached(lifecycle, callback)
    }

}
