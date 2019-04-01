package com.badoo.ribs.core.helper

import com.badoo.ribs.base.leaf.LeafInteractor
import io.reactivex.disposables.Disposable

class TestInteractor(
    disposables: Disposable?
) : LeafInteractor<TestView>(
    disposables = disposables
)
