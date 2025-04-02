package com.badoo.ribs.samples.retained_instance_store.rib.counter

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.retained_instance_store.utils.SecondsClock
import com.badoo.ribs.store.RetainedInstanceStore
import com.badoo.ribs.store.get

class CounterBuilder : SimpleBuilder<Counter>() {

    override fun build(buildParams: BuildParams<Nothing?>): Counter {

        val clock1 = SecondsClock()
        val clock2 = RetainedInstanceStore.get(
            owner = buildParams.identifier,
            factory = { SecondsClock() },
            disposer = { it.dispose() }
        )

        val presenter = CounterPresenterImpl(
            clock1 = clock1,
            clock2 = clock2
        )

        return CounterNode(
            buildParams = buildParams,
            viewFactory = CounterViewImpl.Factory().invoke(null),
            plugins = listOf(presenter)
        )
    }
}
