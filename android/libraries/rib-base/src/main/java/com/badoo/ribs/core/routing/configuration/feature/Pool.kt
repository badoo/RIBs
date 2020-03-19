package com.badoo.ribs.core.routing.configuration.feature

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationKey

internal typealias Pool<C> = Map<ConfigurationKey, ConfigurationContext<C>>

internal typealias MutablePool<C> = MutableMap<ConfigurationKey, ConfigurationContext<C>>

internal fun <C : Parcelable> poolOf(): Pool<C> = mapOf()

internal fun <C : Parcelable> mutablePoolOf(): MutablePool<C> = mutableMapOf()

internal fun <C : Parcelable> Pool<C>.toMutablePool(): MutablePool<C> = toMutableMap()
