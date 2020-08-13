package com.badoo.ribs.rx

import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.Plugin
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

internal class Disposables(disposables: List<Disposable>) : NodeLifecycleAware {
    private val disposable = CompositeDisposable(disposables)

    override fun onDetach() {
        super.onDetach()
        disposable.dispose()
    }
}

fun disposeOnDetach(vararg disposables: Disposable): Plugin =
    Disposables(disposables.toList())