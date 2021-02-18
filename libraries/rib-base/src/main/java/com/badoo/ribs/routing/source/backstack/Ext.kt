package com.badoo.ribs.routing.source.backstack

import android.os.Parcelable
import com.badoo.ribs.routing.Routing


internal fun <C : Parcelable> BackStack.State<C>.contentIdForPosition(position: Int, content: C): Routing.Identifier =
    Routing.Identifier("Back stack $id #$position = $content")

@SuppressWarnings("LongParameterList")
internal fun <C : Parcelable> BackStack.State<C>.overlayIdForPosition(position: Int, content: C, overlayIndex: Int, overlay: C): Routing.Identifier =
    Routing.Identifier("Back stack $id overlay #$position.$overlayIndex = $content.$overlay")
