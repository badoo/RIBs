package com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example

import android.os.Parcelable
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example.RetainedInstanceExampleRouter.Configuration
import com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example.RetainedInstanceExampleRouter.Configuration.Content

interface RetainedInstanceExamplePresenter {
    fun toggleChildCounterConfig()
}

internal class RetainedInstanceExamplePresenterImpl(
    private val backStack: BackStack<Configuration>
) : ViewAware<RetainedInstanceExampleView>,
    RetainedInstanceExamplePresenter {

    override fun toggleChildCounterConfig() {
        val newConfig = when (backStack.getCurrentConfig()) {
            is Content.Retained -> Content.NotRetained
            is Content.NotRetained -> Content.Retained
        }
        backStack.replace(newConfig)
    }

    private fun <T : Parcelable> BackStack<T>.getCurrentConfig() =
        // FIXME add synchronous property to BackStack too
        requireNotNull(state.current?.routing?.configuration) { IllegalStateException() }
}

