package com.badoo.ribs.android.integrationpoint

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.badoo.ribs.android.AndroidRibViewHost
import com.badoo.ribs.android.activitystarter.ActivityBoundary
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.dialog.AlertDialogLauncher
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.permissionrequester.PermissionRequestBoundary
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.android.store.RetainedInstanceStoreViewModel

open class ActivityIntegrationPoint(
    private val activity: AppCompatActivity,
    savedInstanceState: Bundle?,
    rootViewGroup: () -> ViewGroup,
) : IntegrationPoint(
    lifecycleOwner = activity,
    viewLifecycleOwner = MutableLiveData(activity),
    savedInstanceState = savedInstanceState,
    rootViewHostFactory = { AndroidRibViewHost(rootViewGroup()) }
) {
    private val activityBoundary = ActivityBoundary(activity, requestCodeRegistry)
    private val permissionRequestBoundary = PermissionRequestBoundary(activity, requestCodeRegistry)
    private val retainedInstanceViewModel by activity.viewModels<RetainedInstanceStoreViewModel>()

    override val activityStarter: ActivityStarter
        get() = activityBoundary

    override val permissionRequester: PermissionRequester
        get() = permissionRequestBoundary

    override val dialogLauncher: DialogLauncher =
        AlertDialogLauncher(activity, activity.lifecycle)

    override val isFinishing: Boolean
        get() = retainedInstanceViewModel.isCleared

    init {
        retainedInstanceViewModel // initialize ViewModel
    }

    override fun handleUpNavigation() {
        if (!activity.onNavigateUp()) {
            activity.onBackPressed()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityBoundary.onActivityResult(requestCode, resultCode, data)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionRequestBoundary.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
