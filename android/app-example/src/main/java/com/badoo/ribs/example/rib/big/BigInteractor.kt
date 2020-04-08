package com.badoo.ribs.example.rib.big

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Interactor

class BigInteractor(
    private val router: BigRouter,
    savedInstanceState: Bundle?
) : Interactor<BigView>(
    savedInstanceState = savedInstanceState,
    disposables = null
) {

    override fun onViewCreated(view: BigView, viewLifecycle: Lifecycle) {
        view.accept(BigView.ViewModel("My id: " + id.replace("${BigInteractor::class.java.name}.", "")))
    }
}
