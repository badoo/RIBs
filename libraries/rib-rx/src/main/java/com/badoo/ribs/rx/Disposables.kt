package com.badoo.ribs.rx

import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.Plugin
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

private class Disposables(disposables: List<Disposable>) : NodeLifecycleAware {
    private val disposable = CompositeDisposable(disposables)

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}

fun disposables(vararg disposables: Disposable): Plugin =
    Disposables(disposables.toList())
