package com.badoo.ribs.android.recyclerview

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
import java.util.UUID

internal class Adapter<T : Parcelable>(
    initialEntries: List<Entry<T>>? = null,
    private val router: RecyclerViewHostRouter<T>
) : RecyclerView.Adapter<Adapter.ViewHolder>(),
    Consumer<RecyclerViewHostFeature.State<T>> {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var configurationKey: ConfigurationKey? = null
        var uuid: UUID? = null
    }

    private var items: List<Entry<T>> = initialEntries ?: emptyList()

    override fun getItemCount(): Int =
        items.size

    override fun accept(state: RecyclerViewHostFeature.State<T>) {
        items = state.items

        when (state.lastCommand) {
            null -> { /* No-op when restored from TimeCapsule or genuinely empty state */ }
            is Input.Add ->
                notifyItemInserted(state.items.lastIndex)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = items[position]
        holder.configurationKey = entry.configurationKey
        holder.uuid = entry.uuid

    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val configurationKey = holder.configurationKey!! // at this point it should be bound
        router.add(configurationKey, Item(holder.uuid!!))
        router.activate(configurationKey)
        router.getNodes(configurationKey)!!.forEach { childNode ->
            childNode.attachToView(holder.itemView as FrameLayout)
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val configurationKey = holder.configurationKey!! // at this point it should be bound
        router.deactivate(configurationKey)
        router.getNodes(configurationKey)!!.forEach { childNode ->
            childNode.detachFromView()
        }
        // TODO: this implies we kill the associated RIBs, so if we intend to keep them alive,
        //  we should think about managing their lifecycle some other way to avoid
        //  ending up with lots of child RIBs that are never killed
        router.remove(holder.configurationKey!!)
    }
}
