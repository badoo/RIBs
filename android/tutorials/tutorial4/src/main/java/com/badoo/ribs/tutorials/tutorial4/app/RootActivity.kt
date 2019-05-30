package com.badoo.ribs.tutorials.tutorial4.app

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.ViewGroup
import com.badoo.ribs.android.ActivityStarter
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.directory.Directory
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.tutorials.tutorial4.R
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainer
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.builder.GreetingsContainerBuilder
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
        GreetingsContainerBuilder(
            object : GreetingsContainer.Dependency {
                override fun greetingsContainerOutput(): Consumer<GreetingsContainer.Output> = greetingsContainerOutputConsumer
                override fun ribCustomisation(): Directory = AppRibCustomisations
                override fun activityStarter(): ActivityStarter = activityStarter
                override fun permissionRequester(): PermissionRequester = permissionRequester
                override fun dialogLauncher(): DialogLauncher = this@RootActivity
            }
        ).build()

    private val greetingsContainerOutputConsumer = Consumer<GreetingsContainer.Output> { output ->
        when (output) {
            is GreetingsContainer.Output.GreetingsSaid -> {
                Snackbar.make(rootViewGroup, output.greeting, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
