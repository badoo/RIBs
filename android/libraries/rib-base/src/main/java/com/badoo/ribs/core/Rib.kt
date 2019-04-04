package com.badoo.ribs.core

import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.dialog.DialogLauncher

interface Rib {

    interface Dependency {
        fun ribCustomisation(): Directory
        fun activityStarter(): ActivityStarter
        fun permissionRequester(): PermissionRequester
        fun dialogLauncher(): DialogLauncher
    }
}
