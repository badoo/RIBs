package com.badoo.ribs.routing.source.backstack

import android.os.Parcelable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistory
import com.badoo.ribs.routing.history.RoutingHistoryElement
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random


@Parcelize
data class BackStackFeatureState<C : Parcelable>(
    val id: Int = Random.nextInt(),
    val elements: Elements<C> = emptyList()
) : Parcelable, RoutingHistory<C> {

    val current: RoutingHistoryElement<C>?
        get() = elements.lastOrNull()

    override fun iterator(): Iterator<RoutingHistoryElement<C>> =
        elements.iterator()
}


internal fun <C : Parcelable> BackStackFeatureState<C>.contentIdForPosition(position: Int, content: C): Routing.Identifier =
    Routing.Identifier("Back stack $id #$position = $content")

@SuppressWarnings("LongParameterList")
internal fun <C : Parcelable> BackStackFeatureState<C>.overlayIdForPosition(position: Int, content: C, overlayIndex: Int, overlay: C): Routing.Identifier =
    Routing.Identifier("Back stack $id overlay #$position.$overlayIndex = $content.$overlay")
