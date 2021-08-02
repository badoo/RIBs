package com.badoo.ribs.samples.helloworld.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.samples.helloworld.R
import com.badoo.ribs.samples.helloworld.hello_world.HelloWorld
import com.badoo.ribs.samples.helloworld.hello_world.builder.HelloWorldBuilder

class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Rib =
        HelloWorldBuilder(
            object : HelloWorld.Dependency {
                override val name: String = "world"
            }
        ).build(
            root(savedInstanceState)
        )
}
