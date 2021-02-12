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
import io.reactivex.functions.Consumer

interface DialogsPresenter {

    fun handle(event: DialogsView.Event)
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
                    add(wrap(dialogs.themedDialog).subscribe(dialogEventConsumer))
                    add(wrap(dialogs.simpleDialog).subscribe(dialogEventConsumer))
                    add(wrap(dialogs.lazyDialog).subscribe(dialogEventConsumer))
                    add(wrap(dialogs.ribDialog).subscribe(dialogEventConsumer))
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
                    is Dummy -> disposables.add(wrap(child.output).subscribe(dummyConsumer))
                }
            }
        )
    }

    override fun handle(event: DialogsView.Event) {
        when (event) {
            ShowThemedDialogClicked -> backStack.pushOverlay(Overlay.ThemedDialog)
            ShowSimpleDialogClicked -> backStack.pushOverlay(Overlay.SimpleDialog)
            ShowLazyDialogClicked -> {
                initLazyDialog()
                backStack.pushOverlay(Overlay.LazyDialog)
            }
            ShowRibDialogClicked -> backStack.pushOverlay(Overlay.RibDialog)
        }
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

    private val dialogEventConsumer: Consumer<Dialog.Event> = Consumer {
        when (it) {
            Dialog.Event.Positive -> view?.accept("Dialog - Positive clicked")
            Dialog.Event.Negative -> view?.accept("Dialog - Negative clicked")
            Dialog.Event.Neutral -> view?.accept("Dialog - Neutral clicked")
            Dialog.Event.Cancelled -> view?.accept("Dialog - Cancelled")
        }
    }

    private val dummyConsumer: Consumer<Dummy.Output> = Consumer {
        view?.accept("Button in Dummy RIB clicked")
        backStack.popBackStack()
    }
}
