package com.badoo.ribs.routing

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable


@Parcelize
data class Routing<C : Parcelable>(
    val configuration: C,
    val identifier: Identifier = Identifier(), // FIXME no default
    val meta: Parcelable = NoMeta
) : Parcelable {

    @Parcelize
    data class Identifier(
        val id: Serializable = 0 // FIXME no default // FIXME Parcelable
    ) : Parcelable
}

@Parcelize
object NoMeta : Parcelable
