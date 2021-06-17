package com.badoo.ribs.samples.permissions.rib.child1

import com.badoo.ribs.android.permissionrequester.CanProvidePermissionRequester
import com.badoo.ribs.core.Rib

interface PermissionsSampleChild1 : Rib {

    interface Dependency : CanProvidePermissionRequester
}
