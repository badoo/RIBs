package com.badoo.ribs.store

import com.badoo.ribs.core.Rib
import kotlin.reflect.KClass

/**
 * A simple storage to preserve any objects during configuration change events.
 * `factory` function will be invoked immediately on the same thread
 * only if an object of the same class within the same RIB does not exists.
 * The framework will manage the lifecycle of provided objects
 * and invoke `disposer` function to destroy objects properly.
 *
 * Sample usage:
 * ```kotlin
 * val feature = RetainedInstanceStore.get(
 *     owner = buildParams.identifier,
 *     factory = { FeatureImpl() },
 *     disposer = { feature.dispose() }
 * }
 * ```
 */
interface RetainedInstanceStore {

    fun <T : Any> get(owner: Rib.Identifier, clazz: KClass<*>, disposer: (T) -> Unit, factory: () -> T): T

    fun removeAll(owner: Rib.Identifier)

    companion object : RetainedInstanceStore by RetainedInstanceStoreImpl()

}

