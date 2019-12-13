package com.badoo.ribs.test.util.ribs.root

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import io.reactivex.observers.TestObserver

class TestRootInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, Configuration.Permanent, Configuration.Content, Configuration.Overlay, TestRootView>,
    private val viewLifecycleObserver: TestObserver<Lifecycle.Event>
) : Interactor<TestRootView>(
    savedInstanceState = savedInstanceState,
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
