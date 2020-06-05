package com.badoo.ribs.routing.state.feature

import io.reactivex.ObservableEmitter

internal typealias EffectEmitter<C> = ObservableEmitter<RoutingStatePool.Effect<C>>
