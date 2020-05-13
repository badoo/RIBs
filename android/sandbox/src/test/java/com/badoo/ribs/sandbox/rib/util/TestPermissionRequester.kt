package com.badoo.ribs.sandbox.rib.util

import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.core.Identifiable
import io.reactivex.Observable

class TestPermissionRequester : PermissionRequester {

    override fun checkPermissions(client: Identifiable, permissions: Array<String>): PermissionRequester.CheckPermissionsResult {
        TODO()
    }

    override fun requestPermissions(client: Identifiable, requestCode: Int, permissions: Array<String>) {
        TODO()
    }

    override fun events(client: Identifiable): Observable<PermissionRequester.RequestPermissionsEvent> =
        Observable.empty<PermissionRequester.RequestPermissionsEvent>()
}
