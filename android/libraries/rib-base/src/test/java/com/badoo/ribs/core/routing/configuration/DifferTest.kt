//package com.badoo.ribs.core.routing.configuration
//
//import com.badoo.ribs.core.helper.TestRouter.Configuration
//import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
//import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
//import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
//import com.badoo.ribs.core.helper.TestRouter.Configuration.C4
//import com.badoo.ribs.core.helper.TestRouter.Configuration.C5
//import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Activate
//import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Add
//import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Deactivate
//import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Remove
//import com.badoo.ribs.core.routing.configuration.ConfigurationKey.Content
//import com.badoo.ribs.core.routing.configuration.feature.BackStackFeatureState
//import com.badoo.ribs.core.routing.history.Routing
//import com.badoo.ribs.core.routing.history.RoutingHistory
//import com.badoo.ribs.core.routing.history.RoutingHistoryDiffer
//import com.badoo.ribs.core.routing.history.RoutingHistoryElement
//import org.junit.Assert.assertEquals
//import org.junit.Test
//
//class DifferTest {
//
//    @Test
//    fun `Content -- () » ()`() {
//        val oldStack = backStack()
//        val newStack = backStack()
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = emptySet<ConfigurationCommand<Configuration>>()
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1) » (C1)`() {
//        val oldStack = backStack(C1)
//        val newStack = backStack(C1)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = emptySet<ConfigurationCommand<Configuration>>()
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- () » (C1)`() {
//        val oldStack = backStack()
//        val newStack = backStack(C1)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Add(Content(0, Routing(C1 as Configuration))),
//            Activate(Content(0, Routing(C1 as Configuration)))
//        )
//
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1) » ()`() {
//        val oldStack = backStack(C1)
//        val newStack = backStack()
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(0, Routing(C1 as Configuration))),
//            Remove(Content(0, Routing(C1 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1) » (C1, C2)`() {
//        val oldStack = backStack(C1)
//        val newStack = backStack(C1, C2)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(0, Routing(C1 as Configuration))),
//            Add(Content(1, Routing(C2 as Configuration))),
//            Activate(Content(1, Routing(C2 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1, C2) » ()`() {
//        val oldStack = backStack(C1, C2)
//        val newStack = backStack()
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(1, Routing(C2 as Configuration))),
//            Remove(Content(1, Routing(C2 as Configuration))),
//            Remove(Content(0, Routing(C1 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1, C2) » (C1)`() {
//        val oldStack = backStack(C1, C2)
//        val newStack = backStack(C1)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(1, Routing(C2 as Configuration))),
//            Remove(Content(1, Routing(C2 as Configuration))),
//            Activate(Content(0, Routing(C1 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1, C2) » (C2)`() {
//        val oldStack = backStack(C1, C2)
//        val newStack = backStack(C2)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(1, Routing(C2 as Configuration))),
//            Remove(Content(1, Routing(C2 as Configuration))),
//            Remove(Content(0, Routing(C1 as Configuration))),
//            Add(Content(0, Routing(C2 as Configuration))),
//            Activate(Content(0, Routing(C2 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1, C2) » (C1, C2)`() {
//        val oldStack = backStack(C1, C2)
//        val newStack = backStack(C1, C2)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = emptySet<ConfigurationCommand<Configuration>>()
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1, C2) » (C1, C3)`() {
//        val oldStack = backStack(C1, C2)
//        val newStack = backStack(C1, C3)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(1, Routing(C2 as Configuration))),
//            Remove(Content(1, Routing(C2 as Configuration))),
//            Add(Content(1, Routing(C3 as Configuration))),
//            Activate(Content(1, Routing(C3 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1, C2) » (C1, C2, C3)`() {
//        val oldStack = backStack(C1, C2)
//        val newStack = backStack(C1, C2, C3)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(1, Routing(C2 as Configuration))),
//            Add(Content(2, Routing(C3 as Configuration))),
//            Activate(Content(2, Routing(C3 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1, C2, C3) » (C1, C2)`() {
//        val oldStack = backStack(C1, C2, C3)
//        val newStack = backStack(C1, C2)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(2, Routing(C3 as Configuration))),
//            Remove(Content(2, Routing(C3 as Configuration))),
//            Activate(Content(1, Routing(C2 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1, C2, C3) » (C1)`() {
//        val oldStack = backStack(C1, C2, C3)
//        val newStack = backStack(C1)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(2, Routing(C3 as Configuration))),
//            Remove(Content(2, Routing(C3 as Configuration))),
//            Remove(Content(1, Routing(C2 as Configuration))),
//            Activate(Content(0, Routing(C1 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1, C2, C3) » ()`() {
//        val oldStack = backStack(C1, C2, C3)
//        val newStack = backStack()
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(2, Routing(C3 as Configuration))),
//            Remove(Content(2, Routing(C3 as Configuration))),
//            Remove(Content(1, Routing(C2 as Configuration))),
//            Remove(Content(0, Routing(C1 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1, C2, C3) » (C5)`() {
//        val oldStack = backStack(C1, C2, C3)
//        val newStack = backStack(C5)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(2, Routing(C3 as Configuration))),
//            Remove(Content(2, Routing(C3 as Configuration))),
//            Remove(Content(1, Routing(C2 as Configuration))),
//            Remove(Content(0, Routing(C1 as Configuration))),
//            Add(Content(0, Routing(C5 as Configuration))),
//            Activate(Content(0, Routing(C5 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1, C2, C3) » (C1, C2, C4)`() {
//        val oldStack = backStack(C1, C2, C3)
//        val newStack = backStack(C1, C2, C4)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(2, Routing(C3 as Configuration))),
//            Remove(Content(2, Routing(C3 as Configuration))),
//            Add(Content(2, Routing(C4 as Configuration))),
//            Activate(Content(2, Routing(C4 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//    @Test
//    fun `Content -- (C1, C2, C3) » (C1, C4, C5)`() {
//        val oldStack = backStack(C1, C2, C3)
//        val newStack = backStack(C1, C4, C5)
//        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
//        val expected = setOf<ConfigurationCommand<Configuration>>(
//            Deactivate(Content(2, Routing(C3 as Configuration))),
//            Remove(Content(2, Routing(C3 as Configuration))),
//            Remove(Content(1, Routing(C2 as Configuration))),
//            Add(Content(1, Routing(C4 as Configuration))),
//            Add(Content(2, Routing(C5 as Configuration))),
//            Activate(Content(2, Routing(C5 as Configuration)))
//        )
//        assertEquals(expected, actual)
//    }
//
//
////    @Test
////    fun `Overlays -- (C1, C2, C3) » (C1, C2, C3 {O1}) -- Add single overlay on last element with no overlays`() {
////        val oldStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(),
////            C3 to listOf()
////        )
////        val newStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(),
////            C3 to listOf(Configuration.O1)
////        )
////        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
////        val expected = setOf<ConfigurationCommand<Configuration>>(
////            Add(
////                Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 0, Configuration.O1 as Configuration)))
////            ),
////            Activate(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 0, Configuration.O1 as Configuration))))
////        )
////        assertEquals(expected, actual)
////    }
////
////    @Test
////    fun `Overlays -- (C1, C2, C3) » (C1, C2, C3 {O1, O2}) -- Add a second overlay on last element with a single overlay`() {
////        val oldStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(),
////            C3 to listOf()
////        )
////        val newStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(),
////            C3 to listOf(Configuration.O1, Configuration.O2)
////        )
////        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
////        val expected = setOf<ConfigurationCommand<Configuration>>(
////            Add(
////                Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 0, Configuration.O1 as Configuration)))
////            ),
////            Activate(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 0, Configuration.O1 as Configuration)))),
////            Add(
////                Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 1 ,Configuration.O2 as Configuration)))
////            ),
////            Activate(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 1 ,Configuration.O2 as Configuration))))
////        )
////        assertEquals(expected, actual)
////    }
////
////    @Test
////    fun `Overlays -- (C1, C2, C3 {O1}) » (C1, C2, C3 {O1, O2}) -- Add multiple overlays on last element with no overlays`() {
////        val oldStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(),
////            C3 to listOf(Configuration.O1)
////        )
////        val newStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(),
////            C3 to listOf(Configuration.O1, Configuration.O2)
////        )
////        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
////        val expected = setOf<ConfigurationCommand<Configuration>>(
////            Add(
////                Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 1, Configuration.O2 as Configuration)))
////            ),
////            Activate(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 1, Configuration.O2 as Configuration))))
////        )
////        assertEquals(expected, actual)
////    }
////
////    @Test
////    fun `Overlays -- (C1, C2, C3 {O1, O2}) » (C1, C2, C3 {O1}) -- Remove single overlay on last element with multiple overlays`() {
////        val oldStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(),
////            C3 to listOf(Configuration.O1, Configuration.O2)
////        )
////        val newStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(),
////            C3 to listOf(Configuration.O1)
////        )
////        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
////        val expected = setOf<ConfigurationCommand<Configuration>>(
////            Deactivate(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 1, Configuration.O2 as Configuration)))),
////            Remove(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 1, Configuration.O2 as Configuration))))
////        )
////        assertEquals(expected, actual)
////    }
////
////    @Test
////    fun `Overlays -- (C1, C2, C3 {O1, O2}) » (C1, C2, C3) -- Remove all overlays on last element with multiple overlays`() {
////        val oldStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(),
////            C3 to listOf(Configuration.O1, Configuration.O2)
////        )
////        val newStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(),
////            C3 to listOf()
////        )
////        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
////        val expected = setOf<ConfigurationCommand<Configuration>>(
////            Deactivate(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 1, Configuration.O2 as Configuration)))),
////            Remove(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 1, Configuration.O2 as Configuration)))),
////            Deactivate(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 0, Configuration.O1 as Configuration)))),
////            Remove(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 0, Configuration.O1 as Configuration))))
////        )
////        assertEquals(expected, actual)
////    }
////
////    @Test
////    fun `Overlays -- (C1, C2, C3 {O1, O2}) » (C1, C2) -- Remove last back stack element with multiple overlays`() {
////        val oldStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(),
////            C3 to listOf(Configuration.O1, Configuration.O2)
////        )
////        val newStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf()
////        )
////        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
////        val expected = setOf<ConfigurationCommand<Configuration>>(
////            Deactivate(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 1, Configuration.O2 as Configuration)))),
////            Deactivate(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 0, Configuration.O1 as Configuration)))),
////            Deactivate(Content(2, RoutingElement(C3 as Configuration)),
////            Remove(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 1, Configuration.O2 as Configuration)))),
////            Remove(Overlay(Overlay.Key(Content(2, RoutingElement(C3 as Configuration), 0, Configuration.O1 as Configuration)))),
////            Remove(Content(2, RoutingElement(C3 as Configuration)),
////            Activate(Content(1, RoutingElement(C2 as Configuration))
////        )
////        assertEquals(expected, actual)
////    }
////
////    @Test
////    fun `Overlays -- (C1, C2 {O1, O2}, C3) » (C1, C2 {O1, O2}) -- Going back to previous back stack element with multiple overlays`() {
////        val oldStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(Configuration.O1, Configuration.O2),
////            C3 to listOf()
////        )
////        val newStack = backStackWithOverlays(
////            C1 to listOf(),
////            C2 to listOf(Configuration.O1, Configuration.O2)
////        )
////        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
////        val expected = setOf<ConfigurationCommand<Configuration>>(
////            Deactivate<Configuration>(Content(2, RoutingElement(C3 as Configuration)),
////            Remove<Configuration>(Content(2, RoutingElement(C3 as Configuration)),
////            Activate<Configuration>(Content(1, RoutingElement(C2 as Configuration)),
////            Activate<Configuration>(Overlay(Overlay.Key(Content(1, RoutingElement(C2 as Configuration), 0, Configuration.O1 as Configuration)))),
////            Activate<Configuration>(Overlay(Overlay.Key(Content(1, RoutingElement(C2 as Configuration), 1, Configuration.O2 as Configuration))))
////        )
////        assertEquals(expected, actual)
////    }
//
//    private fun backStack(vararg configurations: Configuration): RoutingHistory<Configuration> =
//        BackStackFeatureState(
//            configurations.mapIndexed { index, configuration ->
//                RoutingHistoryElement(
//                    routing = Routing(
//                        configuration = configuration,
//                        identifier = Routing.Identifier(index)
//                    ),
//                    activation = if (index == configurations.lastIndex) RoutingHistoryElement.Activation.ACTIVE else RoutingHistoryElement.Activation.INACTIVE
//                )
//            }
//        )
//
//
//    private fun backStackWithOverlays(vararg configurations: Pair<Configuration, List<Configuration>>): RoutingHistory<Configuration> =
//        BackStackFeatureState(
//            configurations.map {
//                RoutingHistoryElement(
//                    // FIXME identifier
//                    routing = Routing(
//                        configuration = it.first
//                    ),
//                    overlays = it.second
//                )
//            }
//        )
//}
