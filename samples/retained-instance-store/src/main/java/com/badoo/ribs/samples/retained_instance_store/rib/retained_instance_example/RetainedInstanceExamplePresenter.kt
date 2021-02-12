package com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example

import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example.RetainedInstanceExampleRouter.Configuration
import com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example.RetainedInstanceExampleRouter.Configuration.Content
import com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example.RetainedInstanceExampleView.ButtonState

interface RetainedInstanceExamplePresenter {

    fun toggleConfig()
}

internal class RetainedInstanceExamplePresenterImpl(
    private val backStack: BackStack<Configuration>
) : ViewAware<RetainedInstanceExampleView>,
    RetainedInstanceExamplePresenter {

    private var view: RetainedInstanceExampleView? = null

    override fun onViewCreated(view: RetainedInstanceExampleView, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
            onCreate = {
                this@RetainedInstanceExamplePresenterImpl.view = view
                view.updateButtonState(backStack.getCurrentConfig().toButtonState())
            },
            onDestroy = { this@RetainedInstanceExamplePresenterImpl.view = null }
        )
    }

    override fun toggleConfig() {
        val newConfig = when (backStack.getCurrentConfig()) {
            is Content.Counter -> Content.Default
            is Content.Default -> Content.Counter
        }
        backStack.replace(newConfig)
        view?.updateButtonState(newConfig.toButtonState())
    }

    private fun <T : Parcelable> BackStack<T>.getCurrentConfig() =
        // FIXME add synchronous property to BackStack too
        requireNotNull(state.current?.routing?.configuration) { IllegalStateException() }

    private fun Configuration.toButtonState() = when (this) {
        is Content.Counter -> ButtonState.DestroyCounter
        is Content.Default -> ButtonState.CreateCounter
    }
}

