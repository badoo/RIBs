package com.badoo.ribs.sandbox.rib.util

import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.android.permissionrequester.CanProvidePermissionRequester
import com.badoo.ribs.portal.Portal
import com.badoo.ribs.portal.CanProvidePortal
import com.badoo.ribs.android.dialog.CanProvideDialogLauncher
import com.badoo.ribs.android.dialog.DialogLauncher
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
