package com.badoo.ribs.samples.retained_instance_store.rib.counter

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.samples.retained_instance_store.utils.Clock

interface CounterPresenter

internal class CounterPresenterImpl(private val clock: Clock) : CounterPresenter, ViewAware<CounterView> {

    override fun onViewCreated(view: CounterView, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
            onCreate = {
                view.updateCount(clock.elapsedTicks)
                clock.bindOnTick { view.updateCount(it) }
            },
            onDestroy = {
                clock.unBindOnTick()
            }
        )
        clock.startTimer()
    }
}
