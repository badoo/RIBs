package com.badoo.ribs.samples.android.permissions.rib.parent

import com.badoo.ribs.android.permissionrequester.CanProvidePermissionRequester
import com.badoo.ribs.core.Rib

interface PermissionsExample : Rib {

    interface Dependency : CanProvidePermissionRequester
}
