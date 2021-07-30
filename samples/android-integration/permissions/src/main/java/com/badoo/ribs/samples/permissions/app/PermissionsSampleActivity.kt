package com.badoo.ribs.samples.permissions.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.android.permissionrequester.PermissionRequester
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.permissions.R
import com.badoo.ribs.samples.permissions.rib.parent.PermissionsExample
import com.badoo.ribs.samples.permissions.rib.parent.PermissionsExampleBuilder

class PermissionsSampleActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_permissions_sample)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib {
        val dependency = object : PermissionsExample.Dependency {
            override val permissionRequester: PermissionRequester
                get() = integrationPoint.permissionRequester
        }
        return PermissionsExampleBuilder(dependency).build(root(savedInstanceState))
    }

}
