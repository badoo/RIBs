package com.badoo.ribs.core.plugin.utils.logger

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.plugin.AndroidLifecycleAware
import com.badoo.ribs.core.plugin.BackPressHandler
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.core.plugin.SubtreeViewChangeAware
import com.badoo.ribs.core.plugin.SystemAware
import com.badoo.ribs.core.plugin.UpNavigationHandler
import com.badoo.ribs.core.plugin.ViewLifecycleAware

class Logger(
    private val log: (Rib, String) -> Unit = { rib, event ->
        Log.d("Rib Logger", "$rib: $event")
    },
    private val policy: LoggingPolicy = LoggingPolicy.DenyList(),
    private val ribAware: RibAware<Rib> = RibAwareImpl()
) : RibAware<Rib> by ribAware,
    NodeLifecycleAware,
    ViewLifecycleAware,
    SubtreeChangeAware,
    SubtreeViewChangeAware,
    AndroidLifecycleAware,
    SavesInstanceState,
    SystemAware,
    BackPressHandler,
    SubtreeBackPressHandler,
    UpNavigationHandler {

    override fun onBuild() {
        if (policy.logOnBuild) log(rib, "onBuild")
    }

    override fun onCreate(nodeLifecycle: Lifecycle) {
        if (policy.logOnCreate) log(rib, "onCreate")
    }

    override fun onAttach() {
        super.onAttach()
        if (policy.logOnAttached) log(rib, "onAttached")
    }

    override fun onDestroy() {
        if (policy.logOnDestroy) log(rib, "onDestroy")
    }

    override fun onAttachToView() {
        if (policy.logOnAttachToView) log(rib, "onAttachToView")
    }

    override fun onDetachFromView() {
        if (policy.logOnDetachFromView) log(rib, "onDetachFromView")
    }

    override fun onChildBuilt(child: Node<*>) {
        if (policy.logOnChildBuilt) log(rib, "onChildBuilt: $child")
    }

    override fun onChildAttached(child: Node<*>) {
        if (policy.logOnChildAttached) log(rib, "onChildAttached: $child")
    }

    override fun onChildDetached(child: Node<*>) {
        if (policy.logOnChildDetached) log(rib, "onChildDetached: $child")
    }

    override fun onAttachedChildView(child: Node<*>) {
        if (policy.logOnAttachChildView) log(rib, "onAttachChildView: $child")
    }

    override fun onDetachedChildView(child: Node<*>) {
        if (policy.logOnDetachChildView) log(rib, "onDetachChildView: $child")
    }

    override fun onStart() {
        if (policy.logOnStart) log(rib, "onStart")
    }

    override fun onStop() {
        if (policy.logOnStop) log(rib, "onStop")
    }

    override fun onResume() {
        if (policy.logOnResume) log(rib, "onResume")
    }

    override fun onPause() {
        if (policy.logOnPause) log(rib, "onPause")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (policy.logOnSaveInstanceState) log(rib, "onSaveInstanceState")
    }

    override fun onLowMemory() {
        if (policy.logOnLowMemory) log(rib, "onLowMemory")
    }

    override fun handleBackPress(): Boolean {
        if (policy.logHandleBackPress) log(rib, "handleBackPress")
        return super.handleBackPress()
    }

    override fun handleBackPressFirst(): Boolean {
        if (policy.logHandleBackPressFirst) log(rib, "handleBackPressFirst")
        return super.handleBackPressFirst()
    }

    override fun handleBackPressFallback(): Boolean {
        if (policy.logHandleBackPressFallback) log(rib, "handleBackPressFallback")
        return super.handleBackPressFallback()
    }

    override fun handleUpNavigation(): Boolean {
        if (policy.logHandleUpNavigation) log(rib, "handleUpNavigation")
        return super.handleBackPressFallback()
    }
}
