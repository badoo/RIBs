package com.badoo.ribs.core.helper

import com.badoo.ribs.base.leaf.SingleConfigurationInteractor
import io.reactivex.disposables.Disposable

class TestInteractor(
    disposables: Disposable?
) : SingleConfigurationInteractor<TestView>(
    disposables = disposables
)
