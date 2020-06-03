package com.badoo.ribs.core.routing.state

import android.os.Parcelable
import com.badoo.ribs.core.routing.history.Routing

internal typealias Pool<C> = Map<Routing<C>, RoutingContext<C>>

internal typealias MutablePool<C> = MutableMap<Routing<C>, RoutingContext<C>>

internal fun <C : Parcelable> poolOf(): Pool<C> = mapOf()

internal fun <C : Parcelable> mutablePoolOf(): MutablePool<C> = mutableMapOf()

internal fun <C : Parcelable> Pool<C>.toMutablePool(): MutablePool<C> = toMutableMap()
