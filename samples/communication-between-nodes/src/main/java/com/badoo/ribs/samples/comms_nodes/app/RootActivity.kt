package com.badoo.ribs.samples.comms_nodes.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.samples.comms_nodes.R
import com.badoo.ribs.samples.comms_nodes.rib.greeting_container.builder.GreetingContainerBuilder

class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?) =
        GreetingContainerBuilder().build(BuildContext.root(savedInstanceState))
}