package com.badoo.ribs.base.viewless

import android.os.Parcelable
import com.badoo.ribs.core.Interactor
import io.reactivex.disposables.Disposable

abstract class ViewlessInteractor<C: Parcelable>(
    disposables: Disposable? = null,
    router: ViewlessRouter<C>
): Interactor<C, Nothing>(
    disposables = disposables,
    router = router
)
