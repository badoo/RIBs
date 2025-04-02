package com.badoo.ribs.rx3.adapter

import com.badoo.ribs.minimal.reactive.Source
import io.reactivex.rxjava3.core.Observable

fun <T : Any> Source<T>.rx3(): Observable<T> =
    Observable.create { emitter ->
        val cancellable = observe { emitter.onNext(it) }
        emitter.setCancellable { cancellable.cancel() }
    }
