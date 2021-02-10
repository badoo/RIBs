package com.badoo.ribs.samples.retained_instance_store.rib.retainedInstance

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.samples.retained_instance_store.rib.retainedInstance.routing.RetainedInstanceChildBuilders
import com.badoo.ribs.samples.retained_instance_store.rib.retainedInstance.routing.RetainedInstanceRouter

class RetainedInstanceBuilder(
        private val dependency: RetainedInstance.Dependency
) : SimpleBuilder<RetainedInstance>() {

    private val builders by lazy(LazyThreadSafetyMode.NONE) { RetainedInstanceChildBuilders(dependency) }

    override fun build(buildParams: BuildParams<Nothing?>): RetainedInstance {

        val backStack: BackStack<RetainedInstanceRouter.Configuration> = BackStack(
                initialConfiguration = RetainedInstanceRouter.Configuration.NotRetained,
                buildParams = buildParams
        )
        val presenter = RetainedInstancePresenterImpl(backStack)
        val router = getRouter(buildParams, backStack)
        val viewDependency = object : RetainedInstanceView.Dependency {
            override val presenter: RetainedInstancePresenter = presenter
        }

        return RetainedInstanceNode(
                buildParams = buildParams,
                viewFactory = RetainedInstanceViewImpl.Factory().invoke(viewDependency),
                plugins = listOfNotNull(presenter, router)
        )
    }

    private fun getRouter(
            buildParams: BuildParams<Nothing?>,
            backStack: BackStack<RetainedInstanceRouter.Configuration>
    ): RetainedInstanceRouter =
            RetainedInstanceRouter(
                    buildParams = buildParams,
                    routingSource = backStack,
                    builders = builders
            )

}
