package com.badoo.ribs.test.util

import io.reactivex.Observable
import io.reactivex.observers.TestObserver

fun <T> Observable<T>.subscribeOnTestObserver() = TestObserver<T>().apply {
    subscribe(this)
}
