package com.badoo.ribs

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.common.rib.test.activity.R
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.directory.ViewCustomisationDirectory

class RibTestActivity : RibActivity(), Rib.Dependency {

    override fun ribCustomisation() = ViewCustomisationDirectory()

    override fun activityStarter() = activityStarter

    override fun permissionRequester() = permissionRequester

    override fun dialogLauncher() = this

    override val rootViewGroup: ViewGroup
        get() = findViewById(android.R.id.content)

    override fun createRib(): Node<*> =
        ribFactory!!(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    companion object {
        var ribFactory: ((RibTestActivity) -> Node<*>)? = null
    }
}
