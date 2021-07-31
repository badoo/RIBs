package com.badoo.ribs.example.extensions

import io.reactivex.Observable

fun <T> T?.toObservable(): Observable<T> =
    if (this == null) Observable.empty() else Observable.just(this)
