package com.badoo.ribs.core.helper

import com.badoo.ribs.core.Interactor
import io.reactivex.disposables.Disposable

class TestInteractor(
    disposables: Disposable?
) : Interactor<TestView>(
    disposables = disposables
)
