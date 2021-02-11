package com.badoo.ribs.rx2.adapter

import com.badoo.ribs.core.state.Source
import io.reactivex.Observable

fun <T> Source<T>.rx2(): Observable<T> =
    Observable.create { emitter ->
        val cancellable = observe { emitter.onNext(it) }
        emitter.setCancellable { cancellable.cancel() }
    }
