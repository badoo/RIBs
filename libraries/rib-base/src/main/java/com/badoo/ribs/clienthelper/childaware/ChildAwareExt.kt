package com.badoo.ribs.clienthelper.childaware

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Rib

/** Simplify usage of [ChildAware] by providing default lifecycle to every method. */
fun ChildAware.childAware(lifecycle: Lifecycle, block: ChildAwareScope.() -> Unit) {
    ChildAwareScope(this, lifecycle).block()
}

inline fun <reified T : Rib> ChildAware.whenChildBuilt(
    lifecycle: Lifecycle,
    noinline callback: ChildCallback<T>,
) {
    whenChildBuilt(lifecycle, T::class, callback)
}

inline fun <reified T : Rib> ChildAware.whenChildAttached(
    lifecycle: Lifecycle,
    noinline callback: ChildCallback<T>,
) {
    whenChildAttached(lifecycle, T::class, callback)
}

inline fun <reified T1 : Rib, reified T2 : Rib> ChildAware.whenChildrenBuilt(
    lifecycle: Lifecycle,
    noinline callback: ChildrenCallback<T1, T2>,
) {
    whenChildrenBuilt(lifecycle, T1::class, T2::class, callback)
}

inline fun <reified T1 : Rib, reified T2 : Rib> ChildAware.whenChildrenAttached(
    lifecycle: Lifecycle,
    noinline callback: ChildrenCallback<T1, T2>,
) {
    whenChildrenAttached(lifecycle, T1::class, T2::class, callback)
}
