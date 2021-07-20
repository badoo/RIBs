package com.badoo.ribs.android.recyclerview

import android.content.Context
import android.os.Parcelable
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Input
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.core.Rib
import com.badoo.ribs.routing.resolver.RoutingResolver
import kotlinx.parcelize.Parcelize

@ExperimentalApi
interface RecyclerViewHost<T : Parcelable>: Rib, Connectable<Input<T>, Nothing> {

    @ExperimentalApi
    interface Dependency<T : Parcelable> {
        val hostingStrategy: HostingStrategy
        val initialElements: List<T>
        val resolver: RoutingResolver<T>
        val recyclerViewFactory: RecyclerViewFactory
        val layoutManagerFactory: LayoutManagerFactory
        val viewHolderLayoutParams: FrameLayout.LayoutParams
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
