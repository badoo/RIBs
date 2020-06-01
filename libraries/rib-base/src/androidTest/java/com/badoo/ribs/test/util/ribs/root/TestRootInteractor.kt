package com.badoo.ribs.test.util.ribs.root

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.builder.BuildParams
import io.reactivex.observers.TestObserver

class TestRootInteractor(
    buildParams: BuildParams<Nothing?>,
    private val viewLifecycleObserver: TestObserver<Lifecycle.Event>
) : Interactor<TestRoot, TestRootView>(
    buildParams = buildParams,
    disposables = null
) {

    override fun onViewCreated(view: TestRootView, viewLifecycle: Lifecycle) {
        viewLifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
            fun onEach(owner: LifecycleOwner, event: Lifecycle.Event) {
                viewLifecycleObserver.onNext(event)
            }
        })
    }
}
