package com.badoo.ribs.test.integrationpoint

import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.android.requestcode.RequestCodeClient
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.minimal.reactive.Source

class TestPermissionRequester : PermissionRequester {

    private val eventsRelay = Relay<PermissionRequester.RequestPermissionsEvent>()
    private var givenAll: Boolean = false
    private var grantAll: Boolean = false

    override fun checkPermissions(
        client: RequestCodeClient,
        permissions: Array<String>
    ): PermissionRequester.CheckPermissionsResult =
        PermissionRequester.CheckPermissionsResult(
            granted = if (givenAll) permissions.toList() else emptyList(),
            notGranted = if (givenAll) emptyList() else permissions.toList(),
            shouldShowRationale = emptyList(),
        )

    override fun events(client: RequestCodeClient): Source<PermissionRequester.RequestPermissionsEvent> =
        eventsRelay

    override fun requestPermissions(
        client: RequestCodeClient,
        requestCode: Int,
        permissions: Array<String>
    ) {
        eventsRelay.accept(
            PermissionRequester.RequestPermissionsEvent.RequestPermissionsResult(
                requestCode = requestCode,
                granted = if (grantAll) permissions.toList() else emptyList(),
                denied = if (grantAll) emptyList() else permissions.toList(),
            )
        )
    }

    fun permissionsGiven() {
        givenAll = true
    }

    fun permissionsNotGiven() {
        givenAll = false
    }

    fun allowAll() {
        grantAll = true
    }

    fun denyAll() {
        grantAll = false
    }

}
