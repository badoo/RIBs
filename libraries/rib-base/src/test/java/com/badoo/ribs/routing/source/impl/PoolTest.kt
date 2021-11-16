package com.badoo.ribs.routing.source.impl

import android.os.Parcelable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.Routing.Identifier
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.history.RoutingHistoryElement.Activation
import com.badoo.ribs.routing.source.impl.Pool.Item
import com.badoo.ribs.test.emptyBuildParams
import kotlinx.parcelize.Parcelize
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PoolTest {

    private var routingHistoryElements: List<RoutingHistoryElement<Configuration>> = emptyList()
    private var historyUpdatesCount: Int = 0

    @Test
    fun `GIVEN conf1 is active WHEN activateOnly is called for conf2 THEN conf1 is inactive`() {
        val pool = pool()

        pool.activateOnly(conf2)

        assertEquals(false, routingHistoryElements.isActive(Configuration.Conf1))
    }

    @Test
    fun `GIVEN conf1 is active WHEN activateOnly is called for conf2 THEN conf2 is active`() {
        val pool = pool()

        pool.activateOnly(conf2)

        assertEquals(true, routingHistoryElements.isActive(Configuration.Conf2))
    }

    @Test
    fun `GIVEN conf1 is active and conf2 is inactive WHEN activateOnly is called for conf2 THEN routing history is updated only once`() {
        val pool = pool()
        historyUpdatesCount = 0

        pool.activateOnly(conf2)

        assertEquals(1, historyUpdatesCount)
    }

    @Test
    fun `GIVEN conf1 is active WHEN activate is called for conf2 THEN conf1 is active`() {
        val pool = pool()

        pool.activate(conf2)

        assertEquals(true, routingHistoryElements.isActive(Configuration.Conf1))
    }

    @Test
    fun `GIVEN conf2 is inactive WHEN activate is called for conf2 THEN conf2 is active`() {
        val pool = pool()

        pool.activate(conf2)

        assertEquals(true, routingHistoryElements.isActive(Configuration.Conf2))
    }

    @Test
    fun `GIVEN conf1 is active WHEN deactivate is called for conf1 THEN conf1 is inactive`() {
        val pool = pool()

        pool.deactivate(conf1)

        assertEquals(false, routingHistoryElements.isActive(Configuration.Conf1))
    }

    @Test
    fun `GIVEN conf1 is active WHEN deactivate is called for conf2 THEN conf1 is active`() {
        val pool = pool(
            listOf(
                Item(
                    configuration = Configuration.Conf1,
                    identifier = conf1,
                    isActive = true
                ),
                Item(
                    configuration = Configuration.Conf2,
                    identifier = conf2,
                    isActive = true
                )
            )
        )

        pool.deactivate(conf2)

        assertEquals(true, routingHistoryElements.isActive(Configuration.Conf1))
    }

    @Test
    fun `GIVEN pool contains conf1 and conf2 WHEN remove is called for conf2 THEN pool contains only conf1`() {
        val conf1Item = Item(
            configuration = Configuration.Conf1,
            identifier = conf1,
            isActive = true
        )
        val conf2Item = Item(
            configuration = Configuration.Conf2,
            identifier = conf2,
            isActive = true
        )
        val pool = pool(listOf(conf1Item, conf2Item))

        pool.remove(conf2)

        assertEquals(listOf(conf1Item.toRoutingHistoryElement()), routingHistoryElements)
    }

    @Test
    fun `GIVEN pool contains conf1 WHEN add is called for conf2 THEN pool contains conf1 and conf2`() {
        val conf1Item = Item(
            configuration = Configuration.Conf1,
            identifier = conf1,
            isActive = true
        )
        val pool = pool(listOf(conf1Item))

        val conf2Item = Item(
            configuration = Configuration.Conf2,
            identifier = conf2,
            isActive = true
        )
        pool.add(conf2Item)

        assertEquals(listOf(conf1Item.toRoutingHistoryElement(), conf2Item.toRoutingHistoryElement()), routingHistoryElements)
    }

    private fun pool(
        initialItems: List<Item<Configuration>> =
            listOf(
                Item(
                    configuration = Configuration.Conf1,
                    identifier = conf1,
                    isActive = true
                ),
                Item(
                    configuration = Configuration.Conf2,
                    identifier = conf2,
                    isActive = false
                )
            )
    ): Pool<Configuration> =
        Pool(
            initialItems = initialItems,
            allowRepeatingConfigurations = false,
            buildParams = emptyBuildParams()
        ).apply {
            observe { routingHistory ->
                routingHistoryElements = routingHistory.toList()
                historyUpdatesCount++
            }
        }

    private fun List<RoutingHistoryElement<Configuration>>.isActive(configuration: Configuration): Boolean =
        find { it.routing.configuration == configuration }?.activation == Activation.ACTIVE

    private fun Item<Configuration>.toRoutingHistoryElement(): RoutingHistoryElement<Configuration> =
        RoutingHistoryElement(
            routing = Routing(
                configuration = configuration,
                identifier = identifier
            ),
            activation = if (isActive) Activation.ACTIVE else Activation.INACTIVE
        )

    sealed class Configuration : Parcelable {
        @Parcelize
        object Conf1 : Configuration()

        @Parcelize
        object Conf2 : Configuration()

        override fun toString(): String = javaClass.simpleName
    }

    private companion object {
        private val conf1 = Identifier("conf1")
        private val conf2 = Identifier("conf2")
    }
}
