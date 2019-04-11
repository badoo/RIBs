package com.badoo.ribs.example.rib.util

import io.reactivex.ObservableSource
import io.reactivex.observers.TestObserver

fun <T> ObservableSource<T>.subscribeOnTestObserver() = TestObserver<T>().apply {
    subscribe(this)
}