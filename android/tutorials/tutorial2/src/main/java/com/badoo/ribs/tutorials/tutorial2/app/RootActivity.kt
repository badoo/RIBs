package com.badoo.ribs.tutorials.tutorial2.app

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.builder.BuildContext.Companion.root
import com.badoo.ribs.tutorials.tutorial2.R
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainer
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.builder.GreetingsContainerBuilder
import com.google.android.material.snackbar.Snackbar
import io.reactivex.functions.Consumer

/** The tutorial app's single activity */
class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Node<*> =
        GreetingsContainerBuilder(
            object : GreetingsContainer.Dependency {
                override fun greetingsContainerOutput(): Consumer<GreetingsContainer.Output> =
                    Consumer { output ->
                        when (output) {
                            is GreetingsContainer.Output.GreetingsSaid -> {
                                Snackbar.make(rootViewGroup, output.greeting, Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
        ).build(
            buildContext = root(
                savedInstanceState = savedInstanceState
            )
        )
}
