package com.badoo.ribs.samples.retained_instance_store.rib.counter

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.samples.retained_instance_store.utils.Clock

interface CounterPresenter

internal class CounterPresenterImpl(private val clock: Clock) : CounterPresenter, ViewAware<CounterView> {

    private var view: CounterView? = null

    override fun onViewCreated(view: CounterView, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
                onCreate = {
                    this@CounterPresenterImpl.view = view
                    updateCount(clock.elapsedTicks)
                    clock.bindOnTick(::updateCount)
                },
                onDestroy = {
                    this@CounterPresenterImpl.view = null
                }
        )
        clock.starTimer()
    }

    private fun updateCount(counter: Int) {
        view?.updateCount(counter)
    }
}
