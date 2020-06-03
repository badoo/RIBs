package com.badoo.ribs.core.routing.state.feature

import io.reactivex.ObservableEmitter

internal typealias EffectEmitter<C> = ObservableEmitter<ConfigurationFeature.Effect<C>>
