package com.badoo.ribs.test.node

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.test.RibTestHelper
import com.badoo.ribs.test.builder.RibBuilderStub
import com.badoo.ribs.test.integrationpoint.TestIntegrationPoint

class RibNodeTestHelper<out R : Rib>(
    val rib: R,
    additionalPlugins: List<Plugin> = emptyList(),
) : RibTestHelper<R> {
    private val lifecycle = LifecycleRegistry(this)

    val integrationPoint: TestIntegrationPoint = TestIntegrationPoint(this)

    init {
        RibBuilderStub<Nothing?, R>(delegate = { rib }).build(
            buildContext = BuildContext.root(
                savedInstanceState = null,
                defaultPlugins = { additionalPlugins }),
            payload = null,
        )
        integrationPoint.attach(rib)
    }

    override fun getLifecycle(): Lifecycle = lifecycle

    override fun moveTo(state: Lifecycle.State) {
        require(state != Lifecycle.State.INITIALIZED) { "Can't move to INITIALIZED state" }
        require(lifecycle.currentState != Lifecycle.State.DESTROYED || state == Lifecycle.State.DESTROYED) { "Can't move from DESTROYED state" }
        if (lifecycle.currentState == Lifecycle.State.INITIALIZED && state == Lifecycle.State.DESTROYED) {
            lifecycle.currentState = Lifecycle.State.CREATED
        }
        lifecycle.currentState = state
    }

    override fun moveToStateAndCheck(state: Lifecycle.State, block: (R) -> Unit) {
        require(state != Lifecycle.State.INITIALIZED) { "Can't move to INITIALIZED state" }

        val returnTo =
            when (val current = lifecycle.currentState) {
                Lifecycle.State.DESTROYED -> error("Can't move from DESTROYED state")
                Lifecycle.State.INITIALIZED -> Lifecycle.State.DESTROYED
                else -> current
            }

        moveTo(state)
        try {
            block(rib)
        } finally {
            moveTo(returnTo)
        }
    }

}
