package com.badoo.ribs.samples.permissions.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.permissions.R
import com.badoo.ribs.samples.permissions.rib.PermissionsBuilder
import com.badoo.ribs.samples.permissions.rib.PermissionsRib

class PermissionsActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_permissions)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib {
        val dependency = object : PermissionsRib.Dependency {
            override val permissionRequester: PermissionRequester
                get() = integrationPoint.permissionRequester
        }
        return PermissionsBuilder(dependency).build(root(savedInstanceState))
    }

}
