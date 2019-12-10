package com.badoo.ribs.android.recyclerview

import android.os.Parcelable
import com.badoo.ribs.android.recyclerview.client.RecyclerViewRibResolver
import com.badoo.ribs.core.Rib
import io.reactivex.ObservableSource
import kotlinx.android.parcel.Parcelize

interface RecyclerViewHost : Rib {

    interface Dependency<T : Parcelable> {
        fun initialElements(): List<T>
        fun resolver(): RecyclerViewRibResolver<T>
        fun recyclerViewHostInput(): ObservableSource<Input<T>>
    }

    sealed class Input<T : Parcelable> : Parcelable {
        @Parcelize
        data class Add<T : Parcelable>(val element: T): Input<T>()
    }
}
