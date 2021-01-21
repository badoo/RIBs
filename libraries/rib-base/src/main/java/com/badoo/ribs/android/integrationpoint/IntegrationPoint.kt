package com.badoo.ribs.android.integrationpoint

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import com.badoo.ribs.android.AndroidRibViewHost
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.dialog.toAlertDialog
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.android.requestcode.RequestCodeRegistry
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.RibView
import java.util.WeakHashMap

abstract class IntegrationPoint(
    private val lifecycleOwner: LifecycleOwner,
    val savedInstanceState: Bundle?,
    internal val rootViewHost: RibView
) : DialogLauncher {

    constructor(
        lifecycleOwner: LifecycleOwner,
        savedInstanceState: Bundle?,
        rootViewGroup: ViewGroup
    ): this(
        lifecycleOwner = lifecycleOwner,
        savedInstanceState = savedInstanceState,
        rootViewHost = AndroidRibViewHost(rootViewGroup)
    )

    protected abstract val isFinishing: Boolean

    protected val requestCodeRegistry = RequestCodeRegistry(savedInstanceState)

    abstract val activityStarter: ActivityStarter

    abstract val permissionRequester: PermissionRequester

    private val dialogs: WeakHashMap<Dialog<*>, AlertDialog> =
        WeakHashMap()

    private var _root: Rib? = null
    private val root: Rib
        get() = _root ?: error("Root has not been initialised. Did you forget to call attach?")

    fun attach(root: Rib) {
        if (_root != null) error("A root has already been attached to this integration point")
        if (!root.node.isRoot) error("Trying to attach non-root Node")
        this._root = root
        root.node.integrationPoint = this
        subscribeToLifecycle()
    }

    fun subscribeToLifecycle() {
        lifecycleOwner.lifecycle.subscribe(
            onCreate = ::onCreate,
            onStart = ::onStart,
            onResume = ::onResume,
            onPause = ::onPause,
            onStop = ::onStop,
            onDestroy = ::onDestroy
        )
    }

    private fun onCreate() {
        root.node.onCreate()
        rootViewHost.attachChild(root.node)
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

    private fun onDestroy() {
        dialogs.values.forEach { it.dismiss() }
        rootViewHost.detachChild(root.node)
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

    override fun show(dialog: Dialog<*>, onClose: () -> Unit) {
        dialogs[dialog] = dialog.toAlertDialog(rootViewHost.context, onClose).also {
            it.show()
        }
    }

    override fun hide(dialog: Dialog<*>) {
        dialogs[dialog]?.dismiss()
    }
}
