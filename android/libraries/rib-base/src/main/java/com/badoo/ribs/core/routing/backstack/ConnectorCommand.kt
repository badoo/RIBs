package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable

internal sealed class ConnectorCommand<C : Parcelable> {
    abstract val index: Int

    data class Add<C : Parcelable>(override val index: Int, val configuration: C) : ConnectorCommand<C>()
    data class MakeActive<C : Parcelable>(override val index: Int) : ConnectorCommand<C>()
    data class MakePassive<C : Parcelable>(override val index: Int) : ConnectorCommand<C>()
    data class Remove<C : Parcelable>(override val index: Int) : ConnectorCommand<C>()
}
