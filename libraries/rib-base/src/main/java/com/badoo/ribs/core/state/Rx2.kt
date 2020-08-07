package com.badoo.ribs.core.state

import io.reactivex.Observable
import io.reactivex.Single

fun <T> Source<T>.rx2(): Observable<T> =
    Observable.create { emitter ->
        val cancellable = observe { emitter.onNext(it) }
        emitter.setCancellable { cancellable.cancel() }
    }

fun <T> SingleSource<T>.rx2(): Single<T> =
    Single.create { emitter ->
        val cancellable = observe { emitter.onSuccess(it) }
        emitter.setCancellable { cancellable.cancel() }
    }
