package com.badoo.ribs.routing.source.impl

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.minimal.reactive.Cancellable
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.minimal.state.TimeCapsule
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistory
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.history.RoutingHistoryElement.Activation
import com.badoo.ribs.routing.source.RoutingSource
import kotlinx.parcelize.Parcelize
import java.util.UUID

class Pool<C : Parcelable>(
    initialItems: List<Item<C>>,
    private val allowRepeatingConfigurations: Boolean,
    private val timeCapsule: TimeCapsule
) : RoutingSource<C> {

    @Parcelize
    data class State<C : Parcelable>(
        val elements: Map<Routing.Identifier, RoutingHistoryElement<C>> = emptyMap()
    ) : Parcelable

    private var elements: Map<Routing.Identifier, RoutingHistoryElement<C>> = HashMap(timeCapsule.elements())
    private val current: RoutingHistory<C>
        get() = RoutingHistory.from(
            elements.values.toList()
        )

    private val states: Relay<RoutingHistory<C>> = Relay()

    init {
        timeCapsule.register(TIME_CAPSULE_KEY) { State(elements) }
        if (elements.isEmpty()) {
            initElements(initialItems)
        }
    }

    constructor(
        initialItems: List<Item<C>>,
        allowRepeatingConfigurations: Boolean,
        buildParams: BuildParams<*>
    ) : this(
        initialItems = initialItems,
        allowRepeatingConfigurations = allowRepeatingConfigurations,
        timeCapsule = TimeCapsule(buildParams.savedInstanceState)
    )

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        timeCapsule.saveState(outState)
    }

    override fun baseLineState(fromRestored: Boolean): RoutingHistory<C> =
        RoutingHistory.from(timeCapsule.elements<C>().values)

    fun add(item: Item<C>) {
        addWithoutStateUpdate(item)
        updateState()
    }

    private fun addWithoutStateUpdate(item: Item<C>) = addWithoutStateUpdate(
        configuration = item.configuration,
        identifier = item.identifier,
        activation = if (item.isActive) Activation.ACTIVE else Activation.INACTIVE
    )

    private fun addWithoutStateUpdate(
        configuration: C,
        identifier: Routing.Identifier = Routing.Identifier(
            "Set ${System.identityHashCode(this)} #$configuration.${if (allowRepeatingConfigurations) UUID.randomUUID() else "#"}"
        ),
        activation: Activation = Activation.INACTIVE
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
                activation = activation,
                routing = Routing(
                    configuration = configuration,
                    identifier = identifier
                ),
                // TODO consider overlay support -- not needed now, can be added later
                overlays = emptyList()
            )
        )

        return identifier
    }

    override fun remove(identifier: Routing.Identifier) {
        elements[identifier]?.let {
            elements = elements
                .minus(it.routing.identifier)

            updateState()
        }
    }

    fun activate(identifier: Routing.Identifier) {
        if (updateActivation(identifier, Activation.ACTIVE)) {
            updateState()
        }
    }

    fun deactivate(identifier: Routing.Identifier) {
        if (updateActivation(identifier, Activation.INACTIVE)) {
            updateState()
        }
    }

    fun activateOnly(identifier: Routing.Identifier) {
        var isActivationUpdated = false
        elements.entries.forEach { entry ->
            val isActivationUpdatedForElement = updateActivation(
                identifier = entry.key,
                activation = if (entry.key == identifier) Activation.ACTIVE else Activation.INACTIVE
            )
            isActivationUpdated = isActivationUpdated || isActivationUpdatedForElement
        }
        if (isActivationUpdated) {
            updateState()
        }
    }

    private fun updateActivation(identifier: Routing.Identifier, activation: Activation): Boolean {
        elements[identifier]?.let {
            elements = elements
                .minus(it.routing.identifier)
                .plus(
                    it.routing.identifier to it.copy(
                        activation = activation
                    )
                )
            return true
        }
        return false
    }

    private fun initElements(initialItems: List<Item<C>>) {
        initialItems.forEach { item ->
            addWithoutStateUpdate(item)
        }
        updateState()
    }

    private fun updateState() {
        states.emit(current)
    }

    override fun observe(callback: (RoutingHistory<C>) -> Unit): Cancellable {
        callback(current)
        return states.observe(callback)
    }

    data class Item<out C : Parcelable>(
        val configuration: C,
        val identifier: Routing.Identifier,
        val isActive: Boolean
    )

    private companion object {

        private const val TIME_CAPSULE_KEY: String = "POOL_TIME_CAPSULE_KEY"

        private fun <C : Parcelable> TimeCapsule.elements(): Map<Routing.Identifier, RoutingHistoryElement<C>> =
            get<State<C>>(TIME_CAPSULE_KEY)?.elements ?: emptyMap()
    }
}
