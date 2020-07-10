package com.badoo.ribs.android.recyclerview

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.badoo.ribs.android.AndroidRibViewHost
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.HostingStrategy.EAGER
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.HostingStrategy.LAZY
import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Input
import com.badoo.ribs.android.recyclerview.RecyclerViewHostFeature.State.Entry
import com.badoo.ribs.annotation.ExperimentalApi
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.routing.activator.ChildActivator
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.source.impl.Pool
import com.badoo.ribs.util.RIBs.errorHandler
import io.reactivex.functions.Consumer
import java.lang.ref.WeakReference

@ExperimentalApi
internal class Adapter<T : Parcelable>(
    private val hostingStrategy: RecyclerViewHost.HostingStrategy,
    initialEntries: List<Entry<T>>? = null,
    private val routingSource: Pool<T>,
    private val feature: RecyclerViewHostFeature<T>,
    private val viewHolderLayoutParams: FrameLayout.LayoutParams
) : RecyclerView.Adapter<Adapter.ViewHolder>(),
    Consumer<RecyclerViewHostFeature.State<T>>,
    ChildActivator<T> {

    private val holders: MutableMap<Routing.Identifier, WeakReference<ViewHolder>> = mutableMapOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var identifier: Routing.Identifier? = null
        var host: RibView = AndroidRibViewHost(itemView as FrameLayout)
    }

    private var items: List<Entry<T>> = initialEntries ?: emptyList()

    override fun getItemCount(): Int =
        items.size

    override fun accept(state: RecyclerViewHostFeature.State<T>) {
        items = state.items

        when (state.lastCommand) {
            null -> { /* No-op when restored from TimeCapsule or genuinely empty state */ }
            is Input.Add -> {
                eagerAdd(state.items.last())
                notifyItemInserted(state.items.lastIndex)
            }
        }
    }

    private fun eagerAdd(entry: Entry<T>) {
        if (hostingStrategy == EAGER) {
            routingSource.add(entry.element, entry.identifier)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            FrameLayout(parent.context).apply {
                layoutParams = viewHolderLayoutParams
            }
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = items[position]
        holder.identifier = entry.identifier
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val identifier = holder.identifier!! // at this point it should be bound
        holders[identifier] = WeakReference(holder)

        if (hostingStrategy == LAZY) {
            val entry = feature.state.items.find { it.identifier == identifier }!!
            routingSource.add(entry.element, entry.identifier)
        }

        routingSource.activate(identifier)
    }

    override fun activate(routing: Routing<T>, child: Node<*>) {
        viewForRouting(routing)?.attachChild(child)
            ?: errorHandler.handleNonFatalError("Holder is gone! Routing: $routing, child: $child")
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.identifier?.let { identifier ->
            routingSource.deactivate(identifier)
            if (hostingStrategy == LAZY) {
                routingSource.remove(identifier)
            }
        } ?: errorHandler.handleNonFatalError("Holder is not bound! holder: $holder")
    }

    internal fun onDestroy() {
        items.forEach {
            routingSource.deactivate(it.identifier)
        }
    }

    override fun deactivate(routing: Routing<T>, child: Node<*>) {
        child.saveViewState()
        viewForRouting(routing)?.detachChild(child)
    }

    private fun viewForRouting(routing: Routing<T>): RibView? =
        holders[routing.identifier]?.get()?.host
}
