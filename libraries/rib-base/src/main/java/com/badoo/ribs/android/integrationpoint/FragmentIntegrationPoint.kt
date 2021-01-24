package com.badoo.ribs.android.integrationpoint

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.badoo.ribs.android.AndroidRibViewHost
import com.badoo.ribs.android.activitystarter.ActivityBoundary
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.permissionrequester.PermissionRequestBoundary
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.android.store.RetainedInstanceStoreViewModel

class FragmentIntegrationPoint(
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
    private val retainedInstanceViewModel by fragment.viewModels<RetainedInstanceStoreViewModel>()

    override val activityStarter: ActivityStarter
        get() = activityBoundary

    override val permissionRequester: PermissionRequester
        get() = permissionRequestBoundary

    override val isFinishing: Boolean
        get() = retainedInstanceViewModel.isCleared

    init {
        retainedInstanceViewModel // initialize ViewModel
    }

    override fun handleUpNavigation() {
        fragment.requireActivity().onNavigateUp()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityBoundary.onActivityResult(requestCode, resultCode, data)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionRequestBoundary.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
