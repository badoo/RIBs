package com.badoo.ribs.clienthelper.childaware

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.plugin.NodeAware
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import kotlin.reflect.KClass

/**
 * This interface provides simplified access to the Nodes children
 * when you need to create connection between them without any intermediate processing.
 *
 * Does not support Portals properly.
 */
interface ChildAware : SubtreeChangeAware, NodeAware {

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
