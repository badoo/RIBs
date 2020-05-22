package com.badoo.ribs.core.routing.history

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Routing<C : Parcelable>(
    val configuration: C,
    val identifier: Identifier = Identifier(), // TODO consider Any // FIXME no default
    val meta: Serializable = 0

) : Parcelable {

    @Parcelize
    data class Identifier(
        val id: Serializable = 0 // FIXME no default
    ) : Parcelable
}
