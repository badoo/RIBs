package com.badoo.ribs.sandbox.rib.switcher

import android.util.Log
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExample
import com.badoo.ribs.sandbox.rib.foo_bar.FooBar
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration.Content
import com.badoo.ribs.rx2.workflows.RxWorkflowNode
import io.reactivex.Single

class SwitcherNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ((RibView) -> SwitcherView?)?,
    plugins: List<Plugin>,
    private val backStack: BackStack<Configuration>
) : RxWorkflowNode<SwitcherView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), Switcher {
    
    override fun attachHelloWorld(): Single<HelloWorld> =
        attachWorkflow {
            Log.d("WORKFLOW", "Switcher / attachHelloWorld")
            backStack.push(Content.Hello)
        }

    override fun attachDialogExample(): Single<DialogExample> =
        attachWorkflow {
            Log.d("WORKFLOW", "Switcher / attachDialogExample")
            backStack.push(Content.DialogsExample)
        }

    override fun attachFooBar(): Single<FooBar> =
        attachWorkflow {
            Log.d("WORKFLOW", "Switcher / attachFooBar")
            backStack.push(Content.Foo)
        }

    override fun waitForHelloWorld(): Single<HelloWorld> =
        waitForChildAttached<HelloWorld>()
            .doOnSuccess {
                Log.d("WORKFLOW", "Switcher / waitForHelloWorld")
            }

    override fun doSomethingAndStayOnThisNode(): Single<Switcher> =
        executeWorkflow {
            // push wish to feature
            Log.d("WORKFLOW", "Switcher / doSomethingAndStayOnThisNode")
        }
}
