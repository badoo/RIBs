package com.badoo.ribs.samples.comms_nodes_1.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.comms_nodes_1.R
import com.badoo.ribs.samples.comms_nodes_1.rib.menu_example.MenuExample
import com.badoo.ribs.samples.comms_nodes_1.rib.menu_example.MenuExampleBuilder

class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib {
        val dependency = object : MenuExample.Dependency {}
        return MenuExampleBuilder(dependency).build(root(savedInstanceState))
    }

}
