package com.badoo.ribs.core.routing.configuration.feature

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class BackStackFeatureState<C : Parcelable>(
    val backStack: List<BackStackElement<C>> = emptyList()
) : Parcelable {

    val current: BackStackElement<C>?
        get() = backStack.lastOrNull()
}

@Parcelize
data class BackStackElement<C : Parcelable>(
    val configuration: C,
    val overlays: List<C> = emptyList()
) : Parcelable
