package com.badoo.ribs.samples.dialogs.rib.dialogs_example

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.samples.dialogs.R
import com.badoo.ribs.samples.dialogs.dialogs.Dialogs
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsView.Event.ShowThemedDialogClicked
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsView.Event.ShowSimpleDialogClicked
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsView.Event.ShowLazyDialogClicked
import com.badoo.ribs.samples.dialogs.rib.dialogs_example.DialogsView.Event.ShowRibDialogClicked
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable.wrap
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

interface DialogsPresenter {

    fun handle(event: DialogsView.Event)
}

internal class DialogsPresenterImpl(
    private val dialogs: Dialogs,
    private val backStack: BackStack<DialogsRouter.Configuration>
) : DialogsPresenter, ViewAware<DialogsView> {

    private var view: DialogsView? = null
    private val dummyViewInput = BehaviorRelay.createDefault(DialogsView.ViewModel("Dialog examples"))
    private val disposables = CompositeDisposable()

    override fun onViewCreated(view: DialogsView, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
            onCreate = {
                this@DialogsPresenterImpl.view = view
                disposables.apply {
                    add(dummyViewInput.subscribe(view))
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

    override fun handle(event: DialogsView.Event) {
        when (event) {
            ShowThemedDialogClicked -> backStack.pushOverlay(DialogsRouter.Configuration.Overlay.ThemedDialog)
            ShowSimpleDialogClicked -> backStack.pushOverlay(DialogsRouter.Configuration.Overlay.SimpleDialog)
            ShowLazyDialogClicked -> {
                initLazyDialog()
                backStack.pushOverlay(DialogsRouter.Configuration.Overlay.LazyDialog)
            }
            ShowRibDialogClicked -> backStack.pushOverlay(DialogsRouter.Configuration.Overlay.RibDialog)
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
            Dialog.Event.Positive -> dummyViewInput.accept(DialogsView.ViewModel("Dialog - Positive clicked"))
            Dialog.Event.Negative -> dummyViewInput.accept(DialogsView.ViewModel("Dialog - Negative clicked"))
            Dialog.Event.Neutral -> dummyViewInput.accept(DialogsView.ViewModel("Dialog - Neutral clicked"))
            Dialog.Event.Cancelled -> dummyViewInput.accept(DialogsView.ViewModel("Dialog - Cancelled"))
        }
    }
}
