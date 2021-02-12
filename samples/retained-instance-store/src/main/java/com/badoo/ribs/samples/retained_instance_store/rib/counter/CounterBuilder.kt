package com.badoo.ribs.samples.retained_instance_store.rib.counter

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.retained_instance_store.utils.SecondsClock
import com.badoo.ribs.store.RetainedInstanceStore
import com.badoo.ribs.store.get


class CounterBuilder : SimpleBuilder<Counter>() {

    override fun build(buildParams: BuildParams<Nothing?>): Counter {

        val presenter = CounterPresenterImpl(clock = SecondsClock(),
            retainedClock = retainedClock(buildParams.identifier))

        return CounterNode(
            buildParams = buildParams,
            viewFactory = CounterViewImpl.Factory().invoke(null),
            plugins = listOf(presenter)
        )
    }

    private fun retainedClock(ownerId: Rib.Identifier) =
        RetainedInstanceStore.get(
            owner = ownerId,
            factory = { SecondsClock() },
            disposer = { it.dispose() }
        )

}
