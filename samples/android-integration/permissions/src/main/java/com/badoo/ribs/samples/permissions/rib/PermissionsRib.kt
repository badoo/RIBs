package com.badoo.ribs.samples.permissions.rib

import com.badoo.ribs.android.permissionrequester.CanProvidePermissionRequester
import com.badoo.ribs.core.Rib

interface PermissionsRib : Rib {

    interface Dependency : CanProvidePermissionRequester
}
