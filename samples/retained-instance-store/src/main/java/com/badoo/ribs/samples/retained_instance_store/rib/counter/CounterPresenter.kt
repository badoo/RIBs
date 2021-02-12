package com.badoo.ribs.samples.retained_instance_store.rib.counter

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.samples.retained_instance_store.utils.Clock

interface CounterPresenter

internal class CounterPresenterImpl(private val clock: Clock, private val retainedClock: Clock) : CounterPresenter, ViewAware<CounterView> {

    override fun onViewCreated(view: CounterView, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
            onCreate = { bindClocks(view) },
            onDestroy = { unBindClocks() }
        )
        startClocks()
    }

    private fun bindClocks(view: CounterView) {

        view.updateCount(clock.elapsedTicks % 100)
        clock.bindOnTick { view.updateCount(it % 100) }

        view.updateRetainedCount(retainedClock.elapsedTicks % 100)
        retainedClock.bindOnTick { view.updateRetainedCount(it % 100) }
    }

    private fun unBindClocks() {
        clock.unBindOnTick()
        retainedClock.unBindOnTick()
    }

    private fun startClocks() {
        clock.start()
        retainedClock.start()
    }
}
