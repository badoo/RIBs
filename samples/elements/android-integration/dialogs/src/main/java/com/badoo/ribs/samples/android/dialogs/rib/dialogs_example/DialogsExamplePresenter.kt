package com.badoo.ribs.samples.android.dialogs.rib.dialogs_example

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.minimal.reactive.CompositeCancellable
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.samples.android.dialogs.R
import com.badoo.ribs.samples.android.dialogs.dialogs.Dialogs
import com.badoo.ribs.samples.android.dialogs.rib.dialogs_example.DialogsExampleRouter.Configuration.Overlay
import com.badoo.ribs.samples.android.dialogs.rib.dummy.Dummy

interface DialogsExamplePresenter {

    fun handleThemedDialog()
    fun handleSimpleDialog()
    fun handleLazyDialog()
    fun handleRibDialog()
}

internal class DialogsExamplePresenterImpl(
    private val dialogs: Dialogs,
    private val backStack: BackStack<DialogsExampleRouter.Configuration>
) : DialogsExamplePresenter, ViewAware<DialogsExampleView>, SubtreeChangeAware {

    private var view: DialogsExampleView? = null
    private val cancellables = CompositeCancellable()

    override fun onViewCreated(view: DialogsExampleView, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
            onCreate = {
                this@DialogsExamplePresenterImpl.view = view
                cancellables += dialogs.themedDialog.observe { resolveDialogEvents(it, view) }
                cancellables += dialogs.simpleDialog.observe { resolveDialogEvents(it, view) }
                cancellables += dialogs.lazyDialog.observe { resolveDialogEvents(it, view) }
                cancellables += dialogs.ribDialog.observe { resolveDialogEvents(it, view) }
            },
            onDestroy = {
                this@DialogsExamplePresenterImpl.view = null
                cancellables.cancel()
            }
        )
    }

    override fun onChildBuilt(child: Node<*>) {
        child.lifecycle.subscribe(
            onCreate = {
                when (child) {
                    is Dummy -> cancellables += child.output.observe { resolveDummyOutput(view) }
                }
            }
        )
    }

    override fun handleThemedDialog() {
        backStack.pushOverlay(Overlay.ThemedDialog)
    }

    override fun handleLazyDialog() {
        initLazyDialog()
        backStack.pushOverlay(Overlay.LazyDialog)
    }

    override fun handleRibDialog() {
        backStack.pushOverlay(Overlay.RibDialog)
    }

    override fun handleSimpleDialog() {
        backStack.pushOverlay(Overlay.SimpleDialog)
    }

    private fun initLazyDialog() {
        with(dialogs.lazyDialog) {
            title = Text.Resource(R.string.lazy_dialog_title)
            message = Text.Resource(R.string.dialog_text)
            buttons {
                positive(Text.Resource(R.string.dialog_positive_button), Dialog.Event.Positive)
            }
            cancellationPolicy = Dialog.CancellationPolicy.Cancellable(
                event = Dialog.Event.Cancelled,
                cancelOnTouchOutside = true
            )
        }
    }

    private fun resolveDialogEvents(event: Dialog.Event, view: DialogsExampleView) {
        when (event) {
            Dialog.Event.Positive -> view.displayText("Dialog - Positive clicked")
            Dialog.Event.Negative -> view.displayText("Dialog - Negative clicked")
            Dialog.Event.Neutral -> view.displayText("Dialog - Neutral clicked")
            Dialog.Event.Cancelled -> view.displayText("Dialog - Cancelled")
        }
    }

    private fun resolveDummyOutput(view: DialogsExampleView?) {
        view?.displayText("Button in Dummy RIB clicked")
        backStack.popBackStack()
    }
}
