package com.badoo.ribs.android

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.badoo.ribs.android.requestcode.RequestCodeRegistry
import com.badoo.ribs.core.Rib
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.dialog.toAlertDialog
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
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

    protected open lateinit var root: Rib<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestCodeRegistry = RequestCodeRegistry(savedInstanceState)

        root = createRib(savedInstanceState).apply {
            node.onAttach()
            node.attachToView(rootViewGroup)
        }

        if (intent?.action == Intent.ACTION_VIEW) {
            handleDeepLink(intent)
        }
    }

    private val disposables = CompositeDisposable()

    fun handleDeepLink(intent: Intent) {
        workflowFactory.invoke(intent)?.let {
            disposables.add(it.subscribe())
        }
    }

    open val workflowFactory: (Intent) -> Observable<*>? = {
        null
    }

    abstract val rootViewGroup: ViewGroup

    abstract fun createRib(savedInstanceState: Bundle?): Rib<*>

    override fun onStart() {
        super.onStart()
        root.node.onStart()
    }

    override fun onStop() {
        super.onStop()
        root.node.onStop()
    }

    override fun onPause() {
        super.onPause()
        root.node.onPause()
    }

    override fun onResume() {
        super.onResume()
        root.node.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        root.node.onSaveInstanceState(outState)
        requestCodeRegistry.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        root.node.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogs.values.forEach { it.dismiss() }
        root.node.detachFromView()
        root.node.onDetach()
    }

    override fun onBackPressed() {
        if (!root.node.handleBackPress()) {
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
