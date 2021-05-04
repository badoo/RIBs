package com.badoo.ribs.clienthelper.childaware

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Rib

inline fun <reified T : Rib> ChildAware.whenChildBuilt(
    lifecycle: Lifecycle = node.lifecycle,
    noinline callback: ChildCallback<T>,
) {
    whenChildBuilt(lifecycle, T::class, callback)
}

inline fun <reified T : Rib> ChildAware.whenChildAttached(
    lifecycle: Lifecycle = node.lifecycle,
    noinline callback: ChildCallback<T>,
) {
    whenChildAttached(lifecycle, T::class, callback)
}

inline fun <reified T1 : Rib, reified T2 : Rib> ChildAware.whenChildrenBuilt(
    lifecycle: Lifecycle = node.lifecycle,
    noinline callback: ChildrenCallback<T1, T2>,
) {
    whenChildrenBuilt(lifecycle, T1::class, T2::class, callback)
}

inline fun <reified T1 : Rib, reified T2 : Rib> ChildAware.whenChildrenAttached(
    lifecycle: Lifecycle = node.lifecycle,
    noinline callback: ChildrenCallback<T1, T2>,
) {
    whenChildrenAttached(lifecycle, T1::class, T2::class, callback)
}

