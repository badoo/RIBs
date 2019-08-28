package com.badoo.ribs.example.rib.switcher

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content.Foo
import com.badoo.ribs.example.rib.switcher.SwitcherRouter.Configuration.Content.Hello
import io.reactivex.Single

class SwitcherNode(
    viewFactory: ((ViewGroup) -> SwitcherView?)?,
    private val router: SwitcherRouter,
    interactor: SwitcherInteractor,
    savedInstanceState: Bundle?
) : Node<SwitcherView>(
    savedInstanceState = savedInstanceState,
    identifier = object : Switcher {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), Switcher.Workflow {
    
    override fun attachHelloWorld(): Single<HelloWorld.Workflow> =
        attachWorkflow {
            Log.d("WORKFLOW", "Switcher / attachHelloWorld")
            router.push(Hello)
        }

    override fun testCrash(): Single<HelloWorld.Workflow> =
        attachWorkflow {
            // test case: attaching Foo, but expecting HelloWorld by mistake
            Log.d("WORKFLOW", "Switcher / testCrash")
            router.push(Foo)
        }

    override fun waitForHelloWorld(): Single<HelloWorld.Workflow> =
        waitForChildAttached<HelloWorld.Workflow>()
            .doOnSuccess {
                Log.d("WORKFLOW", "Switcher / waitForHelloWorld")
            }

    override fun doSomethingAndStayOnThisNode(): Single<Switcher.Workflow> =
        executeWorkflow {
            // push wish to feature
            Log.d("WORKFLOW", "Switcher / doSomethingAndStayOnThisNode")
        }

    override fun detachFromView() {
        super.detachFromView()
    }
}
