package com.badoo.ribs.android.integrationpoint

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.android.requestcode.RequestCodeRegistry
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.RibView

abstract class IntegrationPoint(
    private val lifecycleOwner: LifecycleOwner,
    private val viewLifecycleOwner: LiveData<LifecycleOwner>,
    protected val savedInstanceState: Bundle?,
    private val rootViewHostFactory: () -> RibView?
) {

    var rootViewHost: RibView? = null
        private set

    protected abstract val isFinishing: Boolean

    protected val requestCodeRegistry = RequestCodeRegistry(savedInstanceState)

    abstract val activityStarter: ActivityStarter

    abstract val permissionRequester: PermissionRequester

    abstract val dialogLauncher: DialogLauncher

    private var _root: Rib? = null
    private val root: Rib
        get() = _root ?: error("Root has not been initialised. Did you forget to call attach?")

    fun attach(root: Rib) {
        if (_root != null) error("A root has already been attached to this integration point")
        if (!root.node.isRoot) error("Trying to attach non-root Node")
        this._root = root
        root.node.integrationPoint = this
        subscribeToLifecycle()
        root.node.onAttachFinished()
    }

    private fun subscribeToLifecycle() {
        lifecycleOwner.lifecycle.subscribe(
            onCreate = ::onCreate,
            onStart = ::onStart,
            onResume = ::onResume,
            onPause = ::onPause,
            onStop = ::onStop,
            onDestroy = ::onDestroy
        )
        viewLifecycleOwner.observe(lifecycleOwner, Observer { viewLifecycle ->
            viewLifecycle.lifecycle.subscribe(
                onCreate = ::onViewCreate,
                onDestroy = ::onViewDestroy
            )
        })
    }

    private fun onCreate() {
        root.node.onCreate()
    }

    private fun onViewCreate() {
        rootViewHost = rootViewHostFactory()
        rootViewHost?.attachChild(root.node)
    }

    private fun onStart() {
        root.node.onStart()
    }

    private fun onResume() {
        root.node.onResume()
    }

    private fun onPause() {
        root.node.onPause()
    }

    private fun onStop() {
        root.node.onStop()
    }

    private fun onViewDestroy() {
        rootViewHost?.detachChild(root.node)
        rootViewHost = null
    }

    private fun onDestroy() {
        root.node.onDestroy(!isFinishing)
    }

    fun onSaveInstanceState(outState: Bundle) {
        root.node.onSaveInstanceState(outState)
        requestCodeRegistry.onSaveInstanceState(outState)
    }

    fun onLowMemory() {
        root.node.onLowMemory()
    }

    fun handleBackPress(): Boolean =
        root.node.handleBackPress()

    abstract fun handleUpNavigation()

}
