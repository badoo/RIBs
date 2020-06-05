package com.badoo.ribs.test.util

import androidx.lifecycle.Lifecycle
import io.reactivex.Observable
import io.reactivex.observers.TestObserver

fun <T> Observable<T>.subscribeOnTestObserver() = TestObserver<T>().apply {
    subscribe(this)
}

class LifecycleObserver: TestObserver<Lifecycle.Event>() {
    fun clear() {
        values.clear()
    }
}
