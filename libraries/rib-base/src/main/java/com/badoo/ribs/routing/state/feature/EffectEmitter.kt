package com.badoo.ribs.routing.state.feature

internal typealias EffectEmitter<C> = (RoutingStatePool.Effect<C>) -> Unit
