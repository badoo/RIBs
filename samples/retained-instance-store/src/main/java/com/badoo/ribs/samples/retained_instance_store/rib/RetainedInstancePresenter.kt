package com.badoo.ribs.samples.retained_instance_store.rib

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.samples.retained_instance_store.utils.Clock

interface RetainedInstancePresenter {

    fun dispose()
}

internal class RetainedInstancePresenterImpl(private val clock: Clock) : RetainedInstancePresenter, ViewAware<RetainedInstanceView> {

    private var view: RetainedInstanceView? = null

    override fun onViewCreated(view: RetainedInstanceView, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
                onCreate = {
                    this@RetainedInstancePresenterImpl.view = view
                    updateCount(clock.elapsedTicks)
                },
                onDestroy = {
                    this@RetainedInstancePresenterImpl.view = null
                }
        )
        clock.starTimer(::updateCount)
    }

    override fun dispose() {
        clock.stopTimer()
    }

    private fun updateCount(counter: Int) {
        view?.updateCount(counter)
    }
}
