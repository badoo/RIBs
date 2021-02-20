package com.badoo.ribs.routing.source.impl

import android.os.Parcelable
import com.badoo.ribs.minimal.reactive.Cancellable
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistory
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.RoutingSource
import java.util.UUID

class Pool<C : Parcelable>(
    private val allowRepeatingConfigurations: Boolean
) : RoutingSource<C> {
    private var elements: Map<Routing.Identifier, RoutingHistoryElement<C>> = emptyMap()
    private val current: RoutingHistory<C>
        get() = RoutingHistory.from(
            elements.values
        )

    private val states: Relay<RoutingHistory<C>> = Relay()

    // FIXME save/restore to bundle to support correct baseLineState
    override fun baseLineState(fromRestored: Boolean): RoutingHistory<C> =
        RoutingHistory.from(
            emptySet()
        )

    fun add(
        configuration: C,
        identifier: Routing.Identifier = Routing.Identifier(
            "Set ${System.identityHashCode(this)} #$configuration.${if (allowRepeatingConfigurations) UUID.randomUUID() else "#"}"
        )
    ): Routing.Identifier {
        if (!allowRepeatingConfigurations) {
            elements
                .filter { it.value.routing.configuration == configuration }
                .map { it.key }
                .firstOrNull()?.let {
                    return it
                }
        }

        elements = elements.plus(
            identifier to RoutingHistoryElement(
                activation = RoutingHistoryElement.Activation.INACTIVE,
                routing = Routing(
                    configuration = configuration,
                    identifier = identifier
                ),
                // TODO consider overlay support -- not needed now, can be added later
                overlays = emptyList()
            )
        )

        updateState()

        return identifier
    }

    fun activate(identifier: Routing.Identifier) {
        updateActivation(identifier,
            RoutingHistoryElement.Activation.ACTIVE
        )
    }

    fun deactivate(identifier: Routing.Identifier) {
        updateActivation(identifier,
            RoutingHistoryElement.Activation.INACTIVE
        )
    }

    private fun updateActivation(identifier: Routing.Identifier, activation: RoutingHistoryElement.Activation) {
        elements[identifier]?.let {
            elements = elements
                .minus(it.routing.identifier)
                .plus(it.routing.identifier to it.copy(
                    activation = activation
                )
            )
            updateState()
        }
    }

    override fun remove(identifier: Routing.Identifier) {
        elements[identifier]?.let {
            elements = elements
                .minus(it.routing.identifier)

            updateState()
        }
    }

    private fun updateState() {
        states.emit(current)
    }

    override fun observe(callback: (RoutingHistory<C>) -> Unit): Cancellable {
        callback(current)
        return states.observe(callback)
    }
}
