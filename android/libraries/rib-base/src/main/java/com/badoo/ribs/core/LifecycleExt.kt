//package com.badoo.ribs.core
//
//import com.badoo.mvicore.android.lifecycle.BaseAndroidBinderLifecycle
//import com.badoo.mvicore.binder.Binder
//import com.badoo.mvicore.binder.lifecycle.Lifecycle
//
//fun Node<*>.attachDetach(f: Binder.() -> Unit) {
//    Binder(AttachDetachBinderLifecycle(this)).apply(f)
//}
//
//class AttachDetachBinderLifecycle(
//    node: Node<*>
//): BaseAndroidBinderLifecycle(
//    androidLifecycle,
//    { sendEvent ->
//        object : DefaultLifecycleObserver {
//            override fun onCreate(owner: LifecycleOwner) {
//                sendEvent(Lifecycle.Event.BEGIN)
//            }
//
//            override fun onDestroy(owner: LifecycleOwner) {
//                sendEvent(Lifecycle.Event.END)
//            }
//        }
//    }
//)
