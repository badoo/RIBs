package com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.retained_instance_store.rib.counter.CounterBuilder
import com.badoo.ribs.samples.retained_instance_store.rib.retained_instance_example.RetainedInstanceExampleRouter.Configuration.Content

class RetainedInstanceExampleBuilder(
) : SimpleBuilder<RetainedInstanceExample>() {

    override fun build(buildParams: BuildParams<Nothing?>): RetainedInstanceExample {

        val backStack: BackStack<RetainedInstanceExampleRouter.Configuration> = BackStack(
            initialConfiguration = Content.Default,
            buildParams = buildParams
        )
        val presenter = RetainedInstanceExamplePresenterImpl(backStack)
        val router = router(buildParams, backStack)
        val viewDependency = object : RetainedInstanceExampleView.Dependency {
            override val presenter: RetainedInstanceExamplePresenter = presenter
        }

        return RetainedInstanceExampleNode(
            buildParams = buildParams,
            viewFactory = RetainedInstanceExampleViewImpl.Factory().invoke(viewDependency),
            plugins = listOfNotNull(presenter, router)
        )
    }

    private fun router(
        buildParams: BuildParams<Nothing?>,
        backStack: BackStack<RetainedInstanceExampleRouter.Configuration>
    ): RetainedInstanceExampleRouter =
        RetainedInstanceExampleRouter(
            buildParams = buildParams,
            routingSource = backStack,
            counterBuilder = counterBuilder()
        )

    private fun counterBuilder(): CounterBuilder {
        return CounterBuilder()
    }

}
