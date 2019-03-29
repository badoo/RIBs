package com.badoo.ribs.base.viewless

import com.badoo.ribs.core.Interactor
import io.reactivex.disposables.Disposable

abstract class ViewlessInteractor(
    disposables: Disposable? = null
): Interactor<Nothing>(
    disposables = disposables
)
