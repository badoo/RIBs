package com.badoo.ribs.android.permissionrequester

interface PermissionRequestResultHandler {

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
}

