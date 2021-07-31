package com.badoo.ribs.samples.retained_instance_store.rib.counter

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.samples.retained_instance_store.utils.Clock

interface CounterPresenter

internal class CounterPresenterImpl(
    private val clock1: Clock,
    private val clock2: Clock
) : CounterPresenter, ViewAware<CounterView> {

    override fun onViewCreated(view: CounterView, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
            onCreate = { bindClocks(view) },
            onDestroy = { unBindClocks() }
        )
        startClocks()
    }

    private fun bindClocks(view: CounterView) {
        view.updateCount(clock1.elapsedTicks % 100)
        clock1.bindOnTick { view.updateCount(it % 100) }

        view.updateRetainedCount(clock2.elapsedTicks % 100)
        clock2.bindOnTick { view.updateRetainedCount(it % 100) }
    }

    private fun unBindClocks() {
        clock1.unBindOnTick()
        clock2.unBindOnTick()
    }

    private fun startClocks() {
        clock1.start()
        clock2.start()
    }
}
