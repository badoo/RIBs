package com.badoo.ribs.android

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.badoo.ribs.android.requestcode.RequestCodeRegistry
import com.badoo.ribs.core.Node
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.dialog.toAlertDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.WeakHashMap

abstract class RibActivity : AppCompatActivity(), DialogLauncher {

    private val dialogs: WeakHashMap<Dialog<*>, AlertDialog> =
        WeakHashMap()

    private lateinit var requestCodeRegistry: RequestCodeRegistry

    val activityStarter: ActivityStarterImpl by lazy {
        ActivityStarterImpl(
            activity = this,
            requestCodeRegistry = requestCodeRegistry
        )
    }

    val permissionRequester: PermissionRequesterImpl by lazy {
        PermissionRequesterImpl(
            activity = this,
            requestCodeRegistry = requestCodeRegistry
        )
    }

    protected open lateinit var rootNode: Node<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestCodeRegistry = RequestCodeRegistry(savedInstanceState)

        rootNode = createRib(savedInstanceState).apply {
            onAttach()
            attachToView(rootViewGroup)
        }

        if (intent?.action == Intent.ACTION_VIEW) {
            handleDeepLink(intent)
        }
    }

    private val disposables = CompositeDisposable()

    fun handleDeepLink(intent: Intent) {
        workflowFactory.invoke(intent)?.let {
            disposables.add(it)
        }
    }

    open val workflowFactory: (Intent) -> Disposable? = {
        null
    }

    abstract val rootViewGroup: ViewGroup

    abstract fun createRib(savedInstanceState: Bundle?): Node<*>

    override fun onStart() {
        super.onStart()
        rootNode.onStart()
    }

    override fun onStop() {
        super.onStop()
        rootNode.onStop()
    }

    override fun onPause() {
        super.onPause()
        rootNode.onPause()
    }

    override fun onResume() {
        super.onResume()
        rootNode.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        rootNode.onSaveInstanceState(outState)
        requestCodeRegistry.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        rootNode.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogs.values.forEach { it.dismiss() }
        rootNode.detachFromView()
        rootNode.onDetach()
    }

    override fun onBackPressed() {
        if (!rootNode.handleBackPress()) {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityStarter.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) =
        permissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults)

    override fun show(dialog: Dialog<*>, onClose: () -> Unit) {
        dialogs[dialog] = dialog.toAlertDialog(this, onClose).also {
            it.show()
        }
    }

    override fun hide(dialog: Dialog<*>) {
        dialogs[dialog]?.dismiss()
    }
}
