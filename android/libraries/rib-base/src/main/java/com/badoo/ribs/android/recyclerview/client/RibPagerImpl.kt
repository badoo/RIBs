//package com.badoo.ribs.android.recyclerview.client
//
//import android.os.Handler
//import com.badoo.ribs.android.recyclerview.Adapter
//import com.badoo.ribs.android.recyclerview.RecyclerViewHostFeature
//import com.badoo.ribs.android.recyclerview.RecyclerViewHost
//
//internal class RibPagerImpl<T>(
//    private val feature: RecyclerViewHostFeature<T>,
//    private val adapter: Adapter<T>
//) : RibPager<T> {
//    private val handler = Handler()
//
//
//    override fun add(element: T) {
//        feature.accept(
//            RecyclerViewHost.Input.Add(
//                element
//            )
//        )
//        val lastIndex = feature.state.items.lastIndex
//        handler.post { adapter.notifyItemInserted(lastIndex) }
//    }
//
////    fun add(index: Int, element: T) {
////        items.add(index, Entry.create(element, configurationKeys))
////        handler.post { adapter?.notifyItemInserted(index) }
////    }
////
////    operator fun set(index: Int, element: T) {
////        // FIXME possible leak: leaving behind previous UUID -> Int -> ConfigurationKey(int) -> RIB
////        items[index] = Entry.create(element, configurationKeys)
////        handler.post { adapter?.notifyItemChanged(index) }
////    }
////
////    fun addAll(elements: Collection<T>) {
////        elements.forEach {
////            add(it)
////        }
////    }
////
////    fun addAll(index: Int, elements: Collection<T>) {
////        elements.forEachIndexed { index, element ->
////            add(index, element)
////        }
////    }
////
////    fun remove(element: T): Boolean {
////        items
////            .find { it == element }
////            ?.let {
////                val index = items.indexOf(it)
////                items.remove(it)
////                handler.post { adapter?.notifyItemRemoved(index) }
////                return true
////            }
////
////        return false
////    }
////
////    fun removeAt(index: Int): T {
////        items
////            .elementAt(index)
////            .let {
////                items.remove(it)
////                handler.post { adapter?.notifyItemRemoved(index) }
////                return it.item
////            }
////    }
////
////    fun removeAll(elements: Collection<T>) {
////        elements.forEach {
////            remove(it)
////        }
////    }
////
////    fun clear() {
////        items.clear()
////        handler.post { adapter?.notifyDataSetChanged() }
////    }
//}
