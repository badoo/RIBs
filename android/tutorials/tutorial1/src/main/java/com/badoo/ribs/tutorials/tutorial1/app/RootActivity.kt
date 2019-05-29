package com.badoo.ribs.tutorials.tutorial1.app

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Node
import com.badoo.ribs.tutorials.tutorial1.R
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorld
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.builder.HelloWorldBuilder
import io.reactivex.functions.Consumer

/** The tutorial app's single activity */
class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(): Node<*> =
        HelloWorldBuilder(
            object : HelloWorld.Dependency {
                override fun helloWorldOutput(): Consumer<HelloWorld.Output> = Consumer {
                    Snackbar.make(rootViewGroup, "Hello world!", Snackbar.LENGTH_SHORT).show()
                }
            }
        ).build()
}
