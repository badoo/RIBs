package com.badoo.ribs.example.rib.util

import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.CanProvideActivityStarter
import com.badoo.ribs.android.CanProvidePermissionRequester
import com.badoo.ribs.core.routing.portal.Portal
import com.badoo.ribs.customisation.CanProvidePortal
import com.badoo.ribs.dialog.CanProvideDialogLauncher
import com.badoo.ribs.dialog.DialogLauncher
import com.nhaarman.mockitokotlin2.mock

class TestDefaultDependencies :
    CanProvideActivityStarter,
    CanProvidePermissionRequester,
    CanProvideDialogLauncher,
    CanProvidePortal {

    val permissionRequester = TestPermissionRequester()
    val activityStarter = TestActivityStarter()

    override fun permissionRequester() = permissionRequester
    override fun activityStarter(): ActivityStarter = activityStarter
    override fun dialogLauncher(): DialogLauncher = mock()
    override fun portal(): Portal.OtherSide = mock()
}
