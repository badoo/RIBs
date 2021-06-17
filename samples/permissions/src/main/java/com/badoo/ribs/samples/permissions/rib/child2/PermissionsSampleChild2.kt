package com.badoo.ribs.samples.permissions.rib.child2

import com.badoo.ribs.android.permissionrequester.CanProvidePermissionRequester
import com.badoo.ribs.core.Rib

interface PermissionsSampleChild2 : Rib {

    interface Dependency : CanProvidePermissionRequester
}
