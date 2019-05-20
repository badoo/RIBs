package com.badoo.ribs.core.routing.backstack.feature

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class BackStackFeatureState<C : Parcelable>(
    val backStack: List<BackStackElement<C>> = emptyList()
) : Parcelable {

    val current: BackStackElement<C>?
        get() = backStack.lastOrNull()

    val currentOverlay: C?
        get() = current?.overlays?.lastOrNull()

    val canPopOverlay: Boolean
        get() = backStack.lastOrNull()?.overlays?.isNotEmpty() == true

    val canPopContent: Boolean
        get() = backStack.size > 1

    val canPop: Boolean
        get() = canPopContent || canPopOverlay
}

@Parcelize
data class BackStackElement<C : Parcelable>(
    val configuration: C,
    val overlays: List<C> = emptyList()
) : Parcelable
