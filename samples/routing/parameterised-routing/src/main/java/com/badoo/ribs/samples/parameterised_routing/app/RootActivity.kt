package com.badoo.ribs.samples.parameterised_routing.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.parameterised_routing.R
import com.badoo.ribs.samples.parameterised_routing.rib.parameterised_routing_example.ParameterisedRoutingExample
import com.badoo.ribs.samples.parameterised_routing.rib.parameterised_routing_example.ParameterisedRoutingExampleBuilder

class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        ParameterisedRoutingExampleBuilder(object : ParameterisedRoutingExample.Dependency {}).build(root(savedInstanceState))
}
