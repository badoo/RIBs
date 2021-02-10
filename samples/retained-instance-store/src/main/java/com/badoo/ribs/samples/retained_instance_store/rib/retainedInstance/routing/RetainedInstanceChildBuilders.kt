package com.badoo.ribs.samples.retained_instance_store.rib.retainedInstance.routing

import com.badoo.ribs.samples.retained_instance_store.rib.counter.Counter
import com.badoo.ribs.samples.retained_instance_store.rib.counter.CounterBuilder
import com.badoo.ribs.samples.retained_instance_store.utils.SecondsClock
import com.badoo.ribs.samples.retained_instance_store.rib.retainedInstance.RetainedInstance


internal open class RetainedInstanceChildBuilders(
        private val dependency: RetainedInstance.Dependency
) {
    val retainedCounter: CounterBuilder
        get() = getCounterChildBuilder(dependency, true)
    val notRetainedCounter: CounterBuilder
        get() = getCounterChildBuilder(dependency, false)

    private fun getCounterChildBuilder(dependency: RetainedInstance.Dependency, isRetained: Boolean): CounterBuilder {
        val childDependency = object : Counter.Dependency {
            override val clock = SecondsClock()
            override val isRetained: Boolean = isRetained
        }

        return CounterBuilder(childDependency)
    }
}