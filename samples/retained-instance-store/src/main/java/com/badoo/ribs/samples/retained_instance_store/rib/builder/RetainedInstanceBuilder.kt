package com.badoo.ribs.samples.retained_instance_store.rib.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.retained_instance_store.rib.*
import com.badoo.ribs.samples.retained_instance_store.rib.RetainedInstanceNode
import com.badoo.ribs.samples.retained_instance_store.rib.RetainedInstancePresenterImpl
import com.badoo.ribs.store.RetainedInstanceStore
import com.badoo.ribs.store.get


class RetainedInstanceBuilder(
        private val dependency: RetainedInstanceRib.Dependency
) : SimpleBuilder<RetainedInstanceRib>() {

    override fun build(buildParams: BuildParams<Nothing?>): RetainedInstanceRib {

        val retainedPresenter = RetainedInstanceStore.get(
                owner = buildParams.identifier,
                factory = { RetainedInstancePresenterImpl(dependency.orientationController) },
                disposer = { it.dispose() }
        )
        val viewDependencies: RetainedInstanceView.Dependency = object : RetainedInstanceView.Dependency {
            override val presenter: RetainedInstancePresenter = retainedPresenter
        }

        return RetainedInstanceNode(
                buildParams = buildParams,
                viewFactory = RetainedInstanceViewImpl.Factory().invoke(deps = viewDependencies),
                plugins = listOf(retainedPresenter)
        )
    }
}
