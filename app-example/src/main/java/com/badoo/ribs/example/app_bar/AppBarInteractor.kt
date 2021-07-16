package com.badoo.ribs.example.app_bar

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.app_bar.AppBarBuilder.Params
import com.badoo.ribs.example.app_bar.mapper.UserToViewModel
import com.badoo.ribs.example.app_bar.mapper.ViewEventToOutput
import com.badoo.ribs.example.network.model.User
import com.badoo.ribs.example.repository.UserRepository
import io.reactivex.ObservableSource

internal class AppBarInteractor(
    buildParams: BuildParams<Params>,
    userRepository: UserRepository
) : Interactor<AppBar, AppBarView>(
    buildParams = buildParams
) {

    private val userSource: ObservableSource<User> =
        userRepository
            .getUserById(buildParams.payload.userId)
            .toObservable()

    override fun onViewCreated(view: AppBarView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(view to rib.output using ViewEventToOutput)
            bind(userSource to view using UserToViewModel)
        }
    }
}
