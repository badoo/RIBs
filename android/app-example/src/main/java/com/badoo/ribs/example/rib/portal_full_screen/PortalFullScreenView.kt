package com.badoo.ribs.example.rib.portal_full_screen

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenView.Event
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenView.ViewModel
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreen
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface PortalFullScreenView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val text: String
    )

    interface Factory : ViewFactory<Nothing?, PortalFullScreenView>
}


class PortalFullScreenViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : PortalFullScreenView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_portal_full_screen
    ) : PortalFullScreenView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> PortalFullScreenView = {
            PortalFullScreenViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    private val idText = androidView.findViewById<TextView>(R.id.big_id)
    private val smallContainer = androidView.findViewById<ViewGroup>(R.id.small_container)

    override fun accept(vm: ViewModel) {
        idText.text = vm.text
    }

    override fun getParentViewForChild(child: Rib): ViewGroup? =
        when (child) {
            is PortalSubScreen -> smallContainer
            else -> null
        }
}
