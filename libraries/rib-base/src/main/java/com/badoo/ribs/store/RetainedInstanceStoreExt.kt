package com.badoo.ribs.store

import com.badoo.ribs.core.Rib

inline fun <reified T : Any> RetainedInstanceStore.get(
    owner: Rib.Identifier,
    noinline disposer: (T) -> Unit = {},
    noinline factory: () -> T
): T =
    get(owner, T::class, disposer, factory)
