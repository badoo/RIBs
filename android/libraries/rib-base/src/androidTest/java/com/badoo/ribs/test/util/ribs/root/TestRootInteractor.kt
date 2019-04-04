package com.badoo.ribs.test.util.ribs.root

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import io.reactivex.observers.TestObserver

class TestRootInteractor(
    router: Router<Configuration, TestRootView>,
    private val viewLifecycleObserver: TestObserver<Lifecycle.Event>
) : Interactor<Configuration, TestRootView>(
    router = router,
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
