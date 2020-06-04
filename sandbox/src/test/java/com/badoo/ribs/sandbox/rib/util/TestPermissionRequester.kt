package com.badoo.ribs.sandbox.rib.util

import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.android.requestcode.RequestCodeClient
import io.reactivex.Observable

class TestPermissionRequester : PermissionRequester {

    override fun checkPermissions(client: RequestCodeClient, permissions: Array<String>): PermissionRequester.CheckPermissionsResult {
        TODO()
    }

    override fun requestPermissions(client: RequestCodeClient, requestCode: Int, permissions: Array<String>) {
        TODO()
    }

    override fun events(client: RequestCodeClient): Observable<PermissionRequester.RequestPermissionsEvent> =
        Observable.empty<PermissionRequester.RequestPermissionsEvent>()
}
