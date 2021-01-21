package com.badoo.ribs.android.integrationpoint

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.badoo.ribs.android.activitystarter.ActivityBoundary
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.permissionrequester.PermissionRequestBoundary
import com.badoo.ribs.android.permissionrequester.PermissionRequester

class ActivityIntegrationPoint(
    private val activity: AppCompatActivity,
    savedInstanceState: Bundle?,
    rootViewGroup: ViewGroup
) : IntegrationPoint(
    lifecycleOwner = activity,
    savedInstanceState = savedInstanceState,
    rootViewGroup = rootViewGroup
) {
    private val activityBoundary: ActivityBoundary by lazy {
        ActivityBoundary(
            activity = activity,
            requestCodeRegistry = requestCodeRegistry
        )
    }

    override val activityStarter: ActivityStarter by lazy {
        activityBoundary
    }

    private val permissionRequestBoundary: PermissionRequestBoundary by lazy {
        PermissionRequestBoundary(
            activity = activity,
            requestCodeRegistry = requestCodeRegistry
        )
    }

    override val permissionRequester: PermissionRequester by lazy {
        permissionRequestBoundary
    }

    override val isFinishing: Boolean
        get() = activity.isFinishing

    override fun handleUpNavigation() {
        activity.onNavigateUp()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityBoundary.onActivityResult(requestCode, resultCode, data)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionRequestBoundary.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
