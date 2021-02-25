package com.badoo.ribs.sandbox.rib.util

import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.android.requestcode.RequestCodeClient
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.minimal.reactive.Source

class TestPermissionRequester : PermissionRequester {

    override fun checkPermissions(client: RequestCodeClient, permissions: Array<String>): PermissionRequester.CheckPermissionsResult {
        TODO()
    }

    override fun requestPermissions(client: RequestCodeClient, requestCode: Int, permissions: Array<String>) {
        TODO()
    }

    override fun events(client: RequestCodeClient): Source<PermissionRequester.RequestPermissionsEvent> =
        Relay()
}
