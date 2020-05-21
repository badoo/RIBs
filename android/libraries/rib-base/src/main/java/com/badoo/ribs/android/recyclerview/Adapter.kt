//package com.badoo.ribs.android.recyclerview
//
//import android.os.Parcelable
//import android.view.View
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import androidx.recyclerview.widget.RecyclerView
//import com.badoo.ribs.android.recyclerview.RecyclerViewHost.HostingStrategy.EAGER
//import com.badoo.ribs.android.recyclerview.RecyclerViewHost.HostingStrategy.LAZY
//import com.badoo.ribs.android.recyclerview.RecyclerViewHost.Input
//import com.badoo.ribs.android.recyclerview.RecyclerViewHostFeature.State.Entry
//import com.badoo.ribs.android.recyclerview.RecyclerViewHostRouter.Configuration
//import com.badoo.ribs.core.routing.configuration.ConfigurationKey
//import io.reactivex.functions.Consumer
//import java.util.UUID
//
//internal class Adapter<T : Parcelable>(
//    private val hostingStrategy: RecyclerViewHost.HostingStrategy,
//    initialEntries: List<Entry<T>>? = null,
//    private val router: RecyclerViewHostRouter<T>,
//    private val viewHolderLayoutParams: FrameLayout.LayoutParams
//) : RecyclerView.Adapter<Adapter.ViewHolder>(),
//    Consumer<RecyclerViewHostFeature.State<T>> {
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var configurationKey: ConfigurationKey<Configuration>? = null
//        var uuid: UUID? = null
//    }
//
//    private var items: List<Entry<T>> = initialEntries ?: emptyList()
//
//    override fun getItemCount(): Int =
//        items.size
//
//    override fun accept(state: RecyclerViewHostFeature.State<T>) {
//        items = state.items
//
//        when (state.lastCommand) {
//            null -> { /* No-op when restored from TimeCapsule or genuinely empty state */ }
//            is Input.Add -> {
//                eagerAdd(state.items.last())
//                notifyItemInserted(state.items.lastIndex)
//            }
//
//        }
//    }
//
//    private fun eagerAdd(entry: Entry<T>) {
//        if (hostingStrategy == EAGER) {
//            router.add(entry.configurationKey)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
//        ViewHolder(
//            FrameLayout(parent.context).apply {
//                layoutParams = viewHolderLayoutParams
//            }
//        )
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val entry = items[position]
//        holder.configurationKey = entry.configurationKey
//        holder.uuid = entry.uuid
//
//    }
//
//    override fun onViewAttachedToWindow(holder: ViewHolder) {
////        super.onViewAttachedToWindow(holder)
////        val configurationKey = holder.configurationKey!! // at this point it should be bound
////        if (hostingStrategy == LAZY) {
////            router.add(configurationKey)
////        }
////        router.activate(configurationKey)
////        router.getNodes(configurationKey)!!.forEach { childNode ->
////            childNode.attachToView(holder.itemView as FrameLayout)
////        }
//    }
//
//    override fun onViewRecycled(holder: ViewHolder) {
////        super.onViewRecycled(holder)
////        val configurationKey = holder.configurationKey!! // at this point it should be bound
////        deactivate(configurationKey)
////        if (hostingStrategy == LAZY) {
////            router.remove(configurationKey)
////        }
//    }
//
//    internal fun onDestroy() {
////        items.forEach {
////            deactivate(it.configurationKey)
////        }
//    }
//
////    private fun deactivate(configurationKey: ConfigurationKey<Configuration>) {
////        router.deactivate(configurationKey)
////        router.getNodes(configurationKey)!!.forEach { childNode ->
////            childNode.detachFromView()
////        }
////    }
//}
