package com.badoo.ribs.test.util

import androidx.lifecycle.Lifecycle
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver

fun <T : Any> Observable<T>.subscribeOnTestObserver() = TestObserver<T>().apply {
    subscribe(this)
}

class LifecycleObserver : TestObserver<Lifecycle.Event>() {
    fun clear() {
        values.clear()
    }
}
