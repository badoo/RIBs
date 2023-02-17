package com.badoo.ribs.android.integrationpoint

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.badoo.ribs.android.AndroidRibViewHost
import com.badoo.ribs.android.activitystarter.ActivityBoundary
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.dialog.AlertDialogLauncher
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.permissionrequester.PermissionRequestBoundary
import com.badoo.ribs.android.permissionrequester.PermissionRequester

open class FragmentIntegrationPoint(
    private val fragment: Fragment,
    savedInstanceState: Bundle?,
    private val ribHostViewProvider: (View?) -> ViewGroup? = {
        if (it != null) {
            require(it is ViewGroup)
            it
        } else {
            null
        }
    }
) : IntegrationPoint(
    lifecycleOwner = fragment,
    viewLifecycleOwner = fragment.viewLifecycleOwnerLiveData,
    savedInstanceState = savedInstanceState,
    rootViewHostFactory = { ribHostViewProvider(fragment.requireView())?.let(::AndroidRibViewHost) }
) {
    private val activityBoundary = ActivityBoundary(fragment, requestCodeRegistry)
    private val permissionRequestBoundary = PermissionRequestBoundary(fragment, requestCodeRegistry)

    override val activityStarter: ActivityStarter
        get() = activityBoundary

    override val permissionRequester: PermissionRequester
        get() = permissionRequestBoundary

    override val dialogLauncher: DialogLauncher =
        AlertDialogLauncher(fragment.requireContext(), fragment.lifecycle)

    /**
     * For now this property is only used to determine whether retained state should be cleared.
     *
     * A regression occurred as part of an update to the Lifecycle library making the previous
     * approach inconsistent.
     *
     * As the intention is to avoid clearing state on a configuration change, the property now
     * checks whether the configuration change is occurring.
     */
    override val isFinishing: Boolean
        get() = fragment.requireActivity().isChangingConfigurations

    override fun handleUpNavigation() {
        val activity = fragment.requireActivity()
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
