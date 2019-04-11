package com.badoo.ribs.example.rib.util

import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.core.directory.ViewCustomisationDirectory
import com.badoo.ribs.dialog.DialogLauncher
import com.nhaarman.mockitokotlin2.mock

class TestDefaultDependencies : Rib.Dependency {

    val permissionRequester = TestPermissionRequester()
    val activityStarter = TestActivityStarter()

    override fun ribCustomisation(): Directory = ViewCustomisationDirectory()
    override fun permissionRequester() = permissionRequester
    override fun activityStarter(): ActivityStarter = activityStarter
    override fun dialogLauncher(): DialogLauncher = mock()
}
