package com.badoo.ribs.sandbox.rib.util

import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.observers.TestObserver

fun <T : Any> ObservableSource<T>.subscribeOnTestObserver() = TestObserver<T>().apply {
    subscribe(this)
}