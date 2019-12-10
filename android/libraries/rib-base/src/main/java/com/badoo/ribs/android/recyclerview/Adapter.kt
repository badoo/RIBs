package com.badoo.ribs.android.recyclerview

import android.os.Handler
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Input
import com.badoo.ribs.android.recyclerview.RecyclerViewHostFeature.State.Entry
import com.badoo.ribs.android.recyclerview.RecyclerViewHostRouter.Configuration.Content.Item
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import io.reactivex.functions.Consumer

internal class Adapter<T : Parcelable>(
    initialEntries: List<Entry<T>>? = null,
    private val router: RecyclerViewHostRouter<T>
) : RecyclerView.Adapter<Adapter.ViewHolder>(),
    Consumer<RecyclerViewHostFeature.State<T>> {

    val handler = Handler()
    var items: List<Entry<T>> = initialEntries ?: emptyList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var configurationKey: ConfigurationKey? = null
    }

//    override fun accept(news: News<T>) {
//        when (news) {
//            is Executed -> when (news.input) {
//                is Input.Add -> handler.post { notifyItemInserted(items().lastIndex) }
//            }
//        }
//    }

//    init {
//        if (initialInserts > 0) {
//            handler.post {
//                for (i in 0 until initialInserts) {
//                    notifyItemInserted(i)
//                }
//            }
//        }
//    }

    override fun accept(state: RecyclerViewHostFeature.State<T>) {
        items = state.items

        when (state.lastCommand) {
            // When restored from TimeCapsule or genuinely empty state (but then items will be empty)
            null -> handler.post {
//                state.items.forEach {
//                    notifyItemInserted(state.items.lastIndex)
//                }
            }
            is Input.Add -> handler.post {
                notifyItemInserted(state.items.lastIndex)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            FrameLayout(parent.context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
            }
        )

    override fun getItemCount(): Int =
        items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = items[position]
        router.add(
            entry.configurationKey,
            Item(entry.uuid)
        )
        holder.itemView.tag = entry.configurationKey
        holder.configurationKey = entry.configurationKey

    }

//    override fun onViewAttachedToWindow(holder: ViewHolder) {
//        super.onViewAttachedToWindow(holder)
//        Handler().post {
//            router.activate(holder.configurationKey!!)
//        }
//    }
//
//    override fun onViewDetachedFromWindow(holder: ViewHolder) {
//        super.onViewDetachedFromWindow(holder)
//        Handler().post {
//            router.deactivate(holder.configurationKey!!)
//        }
//    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        Handler().post {
            // TODO: this implies we kill the associated RIB, so if we intend to keep it alive,
            //  we should think about managing its lifecycle some other way to avoid
            //  ending up with lots of child RIBs that are never killed
            router.remove(holder.configurationKey!!)
        }
    }
}
