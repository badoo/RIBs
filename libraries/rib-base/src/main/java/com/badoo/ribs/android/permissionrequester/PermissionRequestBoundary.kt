package com.badoo.ribs.android.permissionrequester

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.badoo.ribs.android.permissionrequester.PermissionRequester.CheckPermissionsResult
import com.badoo.ribs.android.permissionrequester.PermissionRequester.RequestPermissionsEvent
import com.badoo.ribs.android.permissionrequester.PermissionRequester.RequestPermissionsEvent.Cancelled
import com.badoo.ribs.android.permissionrequester.PermissionRequester.RequestPermissionsEvent.RequestPermissionsResult
import com.badoo.ribs.android.requestcode.RequestCodeBasedEventStreamImpl
import com.badoo.ribs.android.requestcode.RequestCodeClient
import com.badoo.ribs.android.requestcode.RequestCodeRegistry

class PermissionRequestBoundary(
    private val activity: AppCompatActivity,
    requestCodeRegistry: RequestCodeRegistry
) : RequestCodeBasedEventStreamImpl<RequestPermissionsEvent>(requestCodeRegistry),
    PermissionRequester,
    PermissionRequestResultHandler {

    override fun checkPermissions(
        client: RequestCodeClient,
        permissions: Array<String>
    ) : CheckPermissionsResult {
        val granted = mutableListOf<String>()
        val shouldShowRationale = mutableListOf<String>()
        val canAsk = mutableListOf<String>()

        permissions.forEach { permission ->
            val list = when {
                permission.isGranted() -> granted
                permission.shouldShowRationale() -> shouldShowRationale
                else -> canAsk
            }

            list += permission
        }

        return CheckPermissionsResult(
            granted, canAsk, shouldShowRationale
        )
    }

    private fun String.isGranted(): Boolean =
        ContextCompat.checkSelfPermission(activity, this) == PackageManager.PERMISSION_GRANTED

    private fun String.shouldShowRationale(): Boolean =
        ActivityCompat.shouldShowRequestPermissionRationale(activity, this)

    @TargetApi(Build.VERSION_CODES.M)
    override fun requestPermissions(
        client: RequestCodeClient,
        requestCode: Int,
        permissions: Array<String>
    ) {
        ActivityCompat.requestPermissions(activity,
            permissions,
            client.forgeExternalRequestCode(requestCode)
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isEmpty()) {
            onPermissionRequestCancelled(requestCode)

        } else {
            onPermissionRequestFinished(requestCode, permissions, grantResults)
        }
    }

    private fun onPermissionRequestCancelled(externalRequestCode: Int) {
        publish(
            externalRequestCode,
            Cancelled(
                requestCode = externalRequestCode.toInternalRequestCode()
            )
        )
    }

    private fun onPermissionRequestFinished(
        externalRequestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val (granted, denied) = sortResults(permissions, grantResults)

        publish(
            externalRequestCode,
            RequestPermissionsResult(
                requestCode = externalRequestCode.toInternalRequestCode(),
                granted = granted,
                denied = denied
            )
        )
    }

    private fun sortResults(
        permissions: Array<out String>,
        grantResults: IntArray
    ): Pair<MutableList<String>, MutableList<String>> {
        val granted = mutableListOf<String>()
        val denied = mutableListOf<String>()

        permissions.forEachIndexed { index, permission ->
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                granted.add(permission)
            } else {
                denied.add(permission)
            }
        }

        return granted to denied
    }
}
