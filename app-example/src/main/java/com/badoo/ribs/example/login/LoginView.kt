package com.badoo.ribs.example.login

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.example.R
import com.badoo.ribs.example.login.LoginView.Event
import com.badoo.ribs.example.login.LoginView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface LoginView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )

    interface Factory : ViewFactory<Nothing?, LoginView>
}


class LoginViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : LoginView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_login
    ) : LoginView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> LoginView = {
            LoginViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    override fun accept(vm: LoginView.ViewModel) {
    }
}
