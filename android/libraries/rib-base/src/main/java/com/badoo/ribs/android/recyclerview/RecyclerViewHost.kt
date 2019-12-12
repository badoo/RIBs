package com.badoo.ribs.android.recyclerview

import android.content.Context
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badoo.ribs.android.recyclerview.client.RecyclerViewRibResolver
import com.badoo.ribs.core.Rib
import io.reactivex.ObservableSource
import kotlinx.android.parcel.Parcelize

/**
 * Considered experimental. Handle with care.
 */
interface RecyclerViewHost : Rib {

    interface Dependency<T : Parcelable> {
        fun hostingStrategy(): HostingStrategy
        fun initialElements(): List<T>
        fun recyclerViewHostInput(): ObservableSource<Input<T>>
        fun resolver(): RecyclerViewRibResolver<T>
        fun layoutManagerFactory(): LayoutManagerFactory

        interface LayoutManagerFactory: (Context) -> RecyclerView.LayoutManager {
            companion object {
                val linear = object : LayoutManagerFactory {
                    override fun invoke(context: Context): RecyclerView.LayoutManager =
                        LinearLayoutManager(context)
                }
            }
        }
    }

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

    sealed class Input<T : Parcelable> : Parcelable {
        @Parcelize
        data class Add<T : Parcelable>(val element: T): Input<T>()
    }
}
