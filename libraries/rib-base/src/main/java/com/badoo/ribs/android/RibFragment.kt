package com.badoo.ribs.android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.badoo.ribs.android.activitystarter.ActivityBoundary
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.dialog.toAlertDialog
import com.badoo.ribs.android.permissionrequester.PermissionRequestBoundary
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.android.requestcode.RequestCodeRegistry
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.RibView
import java.util.WeakHashMap

abstract class RibFragment : Fragment(), DialogLauncher {

    private val dialogs: WeakHashMap<Dialog<*>, AlertDialog> = WeakHashMap()

    private lateinit var requestCodeRegistry: RequestCodeRegistry
    private lateinit var activityBoundary: ActivityBoundary
    private lateinit var permissionRequestBoundary: PermissionRequestBoundary

    protected open lateinit var root: Rib
    protected open var rootViewHost: RibView? = null

    val activityStarter: ActivityStarter
        get() = activityBoundary

    val permissionRequester: PermissionRequester
        get() = permissionRequestBoundary

    abstract fun createRib(savedInstanceState: Bundle?): Rib

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestCodeRegistry = RequestCodeRegistry(savedInstanceState)
        activityBoundary = ActivityBoundary(this, requestCodeRegistry)
        permissionRequestBoundary = PermissionRequestBoundary(this, requestCodeRegistry)
        root = createRib(savedInstanceState)
        root.node.onCreate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = FrameLayout(inflater.context)
        rootViewHost = AndroidRibViewHost(root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootViewHost?.attachChild(root.node)
    }

    override fun onStart() {
        super.onStart()
        root.node.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityBoundary.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionRequestBoundary.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
        root.node.onResume()
    }

    override fun onPause() {
        super.onPause()
        root.node.onPause()
    }

    override fun onStop() {
        super.onStop()
        root.node.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        root.node.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        root.node.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootViewHost?.detachChild(root.node)
        rootViewHost = null
    }

    override fun onDestroy() {
        super.onDestroy()
        root.node.onDestroy()
    }

    override fun show(dialog: Dialog<*>, onClose: () -> Unit) {
        dialogs[dialog] = dialog.toAlertDialog(requireContext(), onClose).also {
            it.show()
        }
    }

    override fun hide(dialog: Dialog<*>) {
        dialogs[dialog]?.dismiss()
    }

}