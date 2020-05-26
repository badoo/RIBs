package com.badoo.ribs.android.recyclerview

import android.content.Context
import android.os.Parcelable
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.badoo.ribs.android.recyclerview.client.RecyclerViewRibResolver
import com.badoo.ribs.core.ExperimentalApi
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.configuration.ConfigurationResolver
import io.reactivex.ObservableSource
import kotlinx.android.parcel.Parcelize

@ExperimentalApi
interface RecyclerViewHost<T : Parcelable>: Rib {

    @ExperimentalApi
    interface Dependency<T : Parcelable> {
        fun hostingStrategy(): HostingStrategy
        fun initialElements(): List<T>
        fun recyclerViewHostInput(): ObservableSource<Input<T>>
        fun resolver(): ConfigurationResolver<T>
        fun recyclerViewFactory(): RecyclerViewFactory
        fun layoutManagerFactory(): LayoutManagerFactory
        fun viewHolderLayoutParams(): FrameLayout.LayoutParams
    }

    @ExperimentalApi
    enum class HostingStrategy {
        /**
         * Child RIBs get created immediately and are only destroyed along with host
         */
        EAGER,

        /**
         * Child RIBs get created when their associated ViewHolders are attached, and get destroyed
         * along with them
         */
        LAZY
    }

    @ExperimentalApi
    sealed class Input<T : Parcelable> : Parcelable {
        @Parcelize
        data class Add<T : Parcelable>(val element: T): Input<T>()
    }
}

typealias RecyclerViewFactory = (Context) -> RecyclerView

typealias LayoutManagerFactory = (Context) -> RecyclerView.LayoutManager
