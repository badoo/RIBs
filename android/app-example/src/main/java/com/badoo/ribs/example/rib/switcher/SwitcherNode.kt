package com.badoo.ribs.example.rib.switcher

import android.util.Log
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.configuration.feature.operation.push
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content.Foo
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content.Hello
import io.reactivex.Single

class SwitcherNode(
    viewFactory: ((ViewGroup) -> SwitcherView?)?,
    private val router: SwitcherRouter,
    interactor: SwitcherInteractor,
    buildParams: BuildParams<*>
) : Node<SwitcherView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), Switcher {
    
    override fun attachHelloWorld(): Single<HelloWorld> =
        attachWorkflow {
            Log.d("WORKFLOW", "Switcher / attachHelloWorld")
            router.push(Hello)
        }

    override fun testCrash(): Single<HelloWorld> =
        attachWorkflow {
            // test case: attaching Foo, but expecting HelloWorld by mistake
            Log.d("WORKFLOW", "Switcher / testCrash")
            router.push(Foo)
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
