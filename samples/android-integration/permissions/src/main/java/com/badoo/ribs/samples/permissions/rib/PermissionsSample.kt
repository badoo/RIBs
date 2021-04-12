package com.badoo.ribs.samples.permissions.rib

import com.badoo.ribs.android.permissionrequester.CanProvidePermissionRequester
import com.badoo.ribs.core.Rib

interface PermissionsSample : Rib {

    interface Dependency : CanProvidePermissionRequester
}
