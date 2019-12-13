package com.badoo.ribs.core.helper

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.disposables.Disposable

class TestInteractor(
    private val onViewCreated: (view: TestView, viewLifecycle: Lifecycle) -> Unit = { _, _ -> },
    savedInstanceState: Bundle? = null,
    router: Router<TestRouter.Configuration, Nothing, TestRouter.Configuration, Nothing, TestView> = mock(),
    disposables: Disposable? = null
) : Interactor<TestView>(
    savedInstanceState = savedInstanceState,
    disposables = disposables
) {

    override fun onViewCreated(view: TestView, viewLifecycle: Lifecycle) {
        onViewCreated.invoke(view, viewLifecycle)
    }
}
