package com.badoo.ribs.samples.dialogs.rib.dialogs_example

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.samples.dialogs.R
import com.badoo.ribs.samples.dialogs.dialogs.Dialogs
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsRouter.Configuration.Overlay
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsView.Event.ShowLazyDialogClicked
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsView.Event.ShowSimpleDialogClicked
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsView.Event.ShowRibDialogClicked
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsView.Event.ShowThemedDialogClicked
import com.badoo.ribs.samples.dialogs.rib.dummy.Dummy
import io.reactivex.Observable.wrap
import io.reactivex.disposables.CompositeDisposable

interface DialogsPresenter {

    fun handleThemedDialog(event: ShowThemedDialogClicked)
    fun handleSimpleDialog(event: ShowSimpleDialogClicked)
    fun handleLazyDialog(event: ShowLazyDialogClicked)
    fun handleRibDialog(event: ShowRibDialogClicked)
}

internal class DialogsPresenterImpl(
    private val dialogs: Dialogs,
    private val backStack: BackStack<DialogsRouter.Configuration>
) : DialogsPresenter, ViewAware<DialogsView>, SubtreeChangeAware {

    private var view: DialogsView? = null
    private val disposables = CompositeDisposable()

    override fun onViewCreated(view: DialogsView, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
            onCreate = {
                this@DialogsPresenterImpl.view = view
                disposables.apply {
                    add(wrap(dialogs.themedDialog).subscribe { resolveDialogEvents(it, view) })
                    add(wrap(dialogs.simpleDialog).subscribe { resolveDialogEvents(it, view) })
                    add(wrap(dialogs.lazyDialog).subscribe { resolveDialogEvents(it, view) })
                    add(wrap(dialogs.ribDialog).subscribe { resolveDialogEvents(it, view) })
                }
            },
            onDestroy = {
                this@DialogsPresenterImpl.view = null
                disposables.clear()
            }
        )
    }

    override fun onChildBuilt(child: Node<*>) {
        child.lifecycle.subscribe(
            onCreate = {
                when (child) {
                    is Dummy -> disposables.add(wrap(child.output).subscribe { resolveDummyOutput(view) })
                }
            }
        )
    }

    override fun handleThemedDialog(event: ShowThemedDialogClicked) {
        backStack.pushOverlay(Overlay.ThemedDialog)
    }

    override fun handleLazyDialog(event: ShowLazyDialogClicked) {
        initLazyDialog()
        backStack.pushOverlay(Overlay.LazyDialog)
    }

    override fun handleRibDialog(event: ShowRibDialogClicked) {
        backStack.pushOverlay(Overlay.RibDialog)
    }

    override fun handleSimpleDialog(event: ShowSimpleDialogClicked) {
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

    private fun resolveDialogEvents(event: Dialog.Event, view: DialogsView) {
        when (event) {
            Dialog.Event.Positive -> view.displayText("Dialog - Positive clicked")
            Dialog.Event.Negative -> view.displayText("Dialog - Negative clicked")
            Dialog.Event.Neutral -> view.displayText("Dialog - Neutral clicked")
            Dialog.Event.Cancelled -> view.displayText("Dialog - Cancelled")
        }
    }

    private fun resolveDummyOutput(view: DialogsView?) {
        view?.displayText("Button in Dummy RIB clicked")
        backStack.popBackStack()
    }
}
