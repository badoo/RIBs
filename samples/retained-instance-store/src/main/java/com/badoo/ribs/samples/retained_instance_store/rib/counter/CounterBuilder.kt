package com.badoo.ribs.samples.retained_instance_store.rib.counter

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.retained_instance_store.rib.counter.*
import com.badoo.ribs.samples.retained_instance_store.rib.counter.CounterNode
import com.badoo.ribs.samples.retained_instance_store.rib.counter.CounterPresenterImpl
import com.badoo.ribs.store.RetainedInstanceStore
import com.badoo.ribs.store.get


class CounterBuilder(
        private val dependency: Counter.Dependency
) : SimpleBuilder<Counter>() {

    override fun build(buildParams: BuildParams<Nothing?>): Counter {

        val presenter = getPresenter(buildParams.identifier, dependency.isRetained)
        val viewDependencies: CounterView.Dependency = object : CounterView.Dependency {
            override val isRetained = dependency.isRetained
        }

        return CounterNode(
                buildParams = buildParams,
                viewFactory = CounterViewImp.Factory().invoke(deps = viewDependencies),
                plugins = listOf(presenter)
        )
    }

    private fun getPresenter(ownerId: Rib.Identifier, isRetained: Boolean): CounterPresenterImpl {

        val clock = if (isRetained) {
            RetainedInstanceStore.get(
                    owner = ownerId,
                    factory = { dependency.clock },
                    disposer = { it.dispose() }
            )
        } else {
            dependency.clock
        }

        return CounterPresenterImpl(clock)
    }
}
