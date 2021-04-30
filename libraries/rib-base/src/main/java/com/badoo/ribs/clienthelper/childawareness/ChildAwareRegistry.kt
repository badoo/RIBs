package com.badoo.ribs.clienthelper.childawareness

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.plugin.NodeAware
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import kotlin.reflect.KClass

/**
 * This interface provides simplified access to the Nodes children
 * when you need to create connection between them without any intermediate processing.
 */
interface ChildAwareRegistry : SubtreeChangeAware, NodeAware {

    fun <T : Rib> whenChildBuilt(
        lifecycle: Lifecycle,
        child: KClass<T>,
        callback: ChildCallback<T>,
    )

    fun <T : Rib> whenChildAttached(
        lifecycle: Lifecycle,
        child: KClass<T>,
        callback: ChildCallback<T>,
    )

    fun <T1 : Rib, T2 : Rib> whenChildrenBuilt(
        lifecycle: Lifecycle,
        child1: KClass<T1>,
        child2: KClass<T2>,
        callback: ChildrenCallback<T1, T2>,
    )

    fun <T1 : Rib, T2 : Rib> whenChildrenAttached(
        lifecycle: Lifecycle,
        child1: KClass<T1>,
        child2: KClass<T2>,
        callback: ChildrenCallback<T1, T2>,
    )

}

inline fun <reified T : Rib> ChildAwareRegistry.whenChildBuilt(
    lifecycle: Lifecycle,
    noinline callback: ChildCallback<T>,
) {
    whenChildBuilt(lifecycle, T::class, callback)
}

inline fun <reified T : Rib> ChildAwareRegistry.whenChildAttached(
    lifecycle: Lifecycle,
    noinline callback: ChildCallback<T>,
) {
    whenChildAttached(lifecycle, T::class, callback)
}

inline fun <reified T1 : Rib, reified T2 : Rib> ChildAwareRegistry.whenChildrenBuilt(
    lifecycle: Lifecycle,
    noinline callback: ChildrenCallback<T1, T2>,
) {
    whenChildrenBuilt(lifecycle, T1::class, T2::class, callback)
}

inline fun <reified T1 : Rib, reified T2 : Rib> ChildAwareRegistry.whenChildrenAttached(
    lifecycle: Lifecycle,
    noinline callback: ChildrenCallback<T1, T2>,
) {
    whenChildrenAttached(lifecycle, T1::class, T2::class, callback)
}
