package com.badoo.ribs.rx3

import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.Plugin
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

private class Disposables(disposables: List<Disposable>) : NodeLifecycleAware {
    private val disposable = CompositeDisposable(disposables)

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}

fun disposables(vararg disposables: Disposable): Plugin =
    Disposables(disposables.toList())
