package com.badoo.ribs.samples.retained_instance_store.rib.retainedInstance

import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.samples.retained_instance_store.rib.retainedInstance.routing.RetainedInstanceRouter
import java.lang.IllegalStateException

interface RetainedInstancePresenter {
    fun toggleChildCounter()
}

internal class RetainedInstancePresenterImpl(
        private val backStack: BackStack<RetainedInstanceRouter.Configuration>
) : ViewAware<RetainedInstanceView>,
        RetainedInstancePresenter {

    private var view: RetainedInstanceView? = null

    override fun onViewCreated(view: RetainedInstanceView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        viewLifecycle.subscribe(
                onCreate = { this.view = view },
                onDestroy = { this.view = null }
        )
    }

    override fun toggleChildCounter() {
        val currentConfig = backStack.getCurrentConfig()
        val newConfig = if (currentConfig is RetainedInstanceRouter.Configuration.Retained) {
            RetainedInstanceRouter.Configuration.NotRetained
        } else {
            RetainedInstanceRouter.Configuration.Retained
        }
        backStack.replace(newConfig)
    }

    private fun <T : Parcelable> BackStack<T>.getCurrentConfig() = requireNotNull(state.current?.routing?.configuration) { IllegalStateException() }
}

