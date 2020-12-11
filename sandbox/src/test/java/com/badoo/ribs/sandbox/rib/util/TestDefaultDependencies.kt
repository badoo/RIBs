package com.badoo.ribs.sandbox.rib.util

import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.android.dialog.CanProvideDialogLauncher
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.permissionrequester.CanProvidePermissionRequester
import com.badoo.ribs.portal.CanProvidePortal
import com.badoo.ribs.portal.Portal
import com.nhaarman.mockitokotlin2.mock

class TestDefaultDependencies :
    CanProvideActivityStarter,
    CanProvidePermissionRequester,
    CanProvideDialogLauncher,
    CanProvidePortal {

    override val permissionRequester: TestPermissionRequester = TestPermissionRequester()
    override val activityStarter: TestActivityStarter = TestActivityStarter()
    override val dialogLauncher: DialogLauncher = mock()
    override val portal: Portal.OtherSide = mock()
}
