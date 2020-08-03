package com.badoo.ribs.core.state

import io.reactivex.Observable

fun <T> Source<T>.wrap(): Observable<T> =
    Observable.create { emitter ->
        val cancellable = observe { emitter.onNext(it) }
        emitter.setCancellable { cancellable.cancel() }
    }
