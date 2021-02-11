package com.badoo.ribs.samples.retained_instance_store.rib.counter

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.retained_instance_store.utils.SecondsClock
import com.badoo.ribs.store.RetainedInstanceStore
import com.badoo.ribs.store.get


class CounterBuilder : Builder<Counter.Params, Counter>() {

    override fun build(buildParams: BuildParams<Counter.Params>): Counter {

        val isRetained = buildParams.payload.isRetained
        val clock = clock(buildParams.identifier, isRetained)
        val presenter = CounterPresenterImpl(clock)
        val viewDependencies: CounterView.Dependency = object : CounterView.Dependency {
            override val isRetained = isRetained
        }

        return CounterNode(
            buildParams = buildParams,
            viewFactory = CounterViewImpl.Factory().invoke(deps = viewDependencies),
            plugins = listOf(presenter)
        )
    }

    private fun clock(ownerId: Rib.Identifier, isRetained: Boolean) = if (isRetained) {
        RetainedInstanceStore.get(
            owner = ownerId,
            factory = { SecondsClock() },
            disposer = { it.dispose() }
        )
    } else {
        SecondsClock()
    }
}
