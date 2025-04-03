package com.badoo.ribs.example.extensions

import io.reactivex.rxjava3.core.Observable

fun <T : Any> T?.toObservable(): Observable<T> =
    if (this == null) Observable.empty() else Observable.just(this)
