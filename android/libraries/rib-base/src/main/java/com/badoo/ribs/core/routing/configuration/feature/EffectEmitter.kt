package com.badoo.ribs.core.routing.configuration.feature

import io.reactivex.ObservableEmitter

internal typealias EffectEmitter<C> = ObservableEmitter<List<ConfigurationFeature.Effect<C>>>
