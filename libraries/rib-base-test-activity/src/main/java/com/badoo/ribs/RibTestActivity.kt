package com.badoo.ribs

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.common.rib.test.activity.R
import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.android.permissionrequester.CanProvidePermissionRequester
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.android.dialog.CanProvideDialogLauncher
import com.badoo.ribs.android.dialog.DialogLauncher

class RibTestActivity : RibActivity(),
    CanProvideActivityStarter,
    CanProvidePermissionRequester,
    CanProvideDialogLauncher {

    override val dialogLauncher: DialogLauncher = this

    override val rootViewGroup: ViewGroup
        get() = findViewById(android.R.id.content)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        ribFactory!!(this, savedInstanceState)

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    companion object {
        var ribFactory: ((RibTestActivity, Bundle?) -> Rib)? = null
    }
}
