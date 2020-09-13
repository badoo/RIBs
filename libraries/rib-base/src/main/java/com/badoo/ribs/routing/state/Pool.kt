package com.badoo.ribs.routing.state

import android.os.Parcelable
import com.badoo.ribs.routing.Routing

internal typealias Pool<C> = Map<Routing<C>, RoutingContext<C>>

internal typealias MutablePool<C> = MutableMap<Routing<C>, RoutingContext<C>>

internal fun <C : Parcelable> poolOf(): Pool<C> = hashMapOf()

internal fun <C : Parcelable> mutablePoolOf(): MutablePool<C> = hashMapOf()

internal fun <C : Parcelable> Pool<C>.toMutablePool(): MutablePool<C> = toMutableMap()
