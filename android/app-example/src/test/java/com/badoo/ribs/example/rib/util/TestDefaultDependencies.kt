package com.badoo.ribs.example.rib.util

import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.CanProvideActivityStarter
import com.badoo.ribs.android.CanProvidePermissionRequester
import com.badoo.ribs.core.customisation.CanProvideRibCustomisation
import com.badoo.ribs.core.customisation.RibCustomisationDirectory
import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.dialog.CanProvideDialogLauncher
import com.badoo.ribs.dialog.DialogLauncher
import com.nhaarman.mockitokotlin2.mock

class TestDefaultDependencies :
    CanProvideActivityStarter,
    CanProvidePermissionRequester,
    CanProvideDialogLauncher,
    CanProvideRibCustomisation {

    val permissionRequester = TestPermissionRequester()
    val activityStarter = TestActivityStarter()

    override fun ribCustomisation(): RibCustomisationDirectory = RibCustomisationDirectoryImpl()
    override fun permissionRequester() = permissionRequester
    override fun activityStarter(): ActivityStarter = activityStarter
    override fun dialogLauncher(): DialogLauncher = mock()
}
