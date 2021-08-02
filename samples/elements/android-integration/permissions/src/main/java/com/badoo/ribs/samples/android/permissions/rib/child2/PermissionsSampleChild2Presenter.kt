package com.badoo.ribs.samples.android.permissions.rib.child2

import android.Manifest
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.android.permissionrequester.PermissionRequester.RequestPermissionsEvent.Cancelled
import com.badoo.ribs.android.permissionrequester.PermissionRequester.RequestPermissionsEvent.RequestPermissionsResult
import com.badoo.ribs.android.requestcode.RequestCodeClient
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.minimal.reactive.Cancellable

interface PermissionsSampleChild2Presenter : RequestCodeClient {

    fun onRequestPermissionsClicked()

    fun onCheckPermissionsClicked()
}

class PermissionsSampleChild2PresenterImpl(
    private val permissionRequester: PermissionRequester,
    private val ribAware: RibAware<PermissionsSampleChild2> = RibAwareImpl()
) : PermissionsSampleChild2Presenter, ViewAware<PermissionsSampleChild2ViewImpl>, RibAware<PermissionsSampleChild2> by ribAware {

    private var view: PermissionsSampleChild2ViewImpl? = null
    private var cancellable: Cancellable? = null

    override fun onViewCreated(view: PermissionsSampleChild2ViewImpl, viewLifecycle: Lifecycle) {
        viewLifecycle.subscribe(
            onCreate = { handleOnCreate(view) },
            onDestroy = { handleOnDestroy() }
        )
    }

    private fun handleOnCreate(view: PermissionsSampleChild2ViewImpl) {
        this.view = view
        cancellable = permissionRequester
            .events(this)
            .observe { event ->
                if (event.requestCode == REQUEST_CODE_CAMERA) {
                    when (event) {
                        is RequestPermissionsResult -> view.setText("Permission event: $event")
                        is Cancelled -> view.setText("Permission request cancelled")
                        else -> Unit
                    }
                }
            }
    }

    private fun handleOnDestroy() {
        this.view = null
        cancellable?.cancel()
    }

    override val requestCodeClientId: String
        get() = this.toString()

    companion object {
        private const val REQUEST_CODE_CAMERA = 1
    }

    override fun onRequestPermissionsClicked() {
        permissionRequester.requestPermissions(
            client = this,
            requestCode = REQUEST_CODE_CAMERA,
            permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
        )
    }

    override fun onCheckPermissionsClicked() {
        val result = permissionRequester.checkPermissions(
            client = this,
            permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
        )
        view?.setText(result.toString())
    }
}
