package com.badoo.ribs.rx2.adapter

import com.badoo.ribs.minimal.reactive.Source
import io.reactivex.Observable

fun <T: Any> Source<T>.rx2(): Observable<T> =
    Observable.create { emitter ->
        val cancellable = observe { emitter.onNext(it) }
        emitter.setCancellable { cancellable.cancel() }
    }
