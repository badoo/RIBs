package com.badoo.ribs.samples.gallery.rib.routing.routing_container.routing

import com.badoo.ribs.samples.routing.back_stack.rib.back_stack_example.BackStackExample
import com.badoo.ribs.samples.routing.back_stack.rib.back_stack_example.BackStackExampleBuilder
import com.badoo.ribs.samples.gallery.rib.routing.routing_container.RoutingContainer
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPickerBuilder
import com.badoo.ribs.samples.routing.parameterised_routing.rib.parameterised_routing_example.ParameterisedRoutingExample
import com.badoo.ribs.samples.routing.parameterised_routing.rib.parameterised_routing_example.ParameterisedRoutingExampleBuilder
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.SimpleRoutingParent
import com.badoo.ribs.samples.routing.simple_routing.rib.simple_routing_parent.builder.SimpleRoutingParentBuilder
import com.badoo.ribs.samples.routing.transition_animations.rib.transition_animations_example.TransitionAnimationsExample
import com.badoo.ribs.samples.routing.transition_animations.rib.transition_animations_example.TransitionAnimationsExampleBuilder

internal class RoutingContainerChildBuilders(
    dependency: RoutingContainer.Dependency
) {
    private val subtreeDeps = SubtreeDependency(dependency)

    val picker = RoutingPickerBuilder(subtreeDeps)
    val simpleRouting = SimpleRoutingParentBuilder(subtreeDeps)
    val backStackExample = BackStackExampleBuilder(subtreeDeps)
    val buildTimeDepsExample = ParameterisedRoutingExampleBuilder(subtreeDeps)
    val transitionExample = TransitionAnimationsExampleBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: RoutingContainer.Dependency
    ) : RoutingContainer.Dependency by dependency,
        RoutingPicker.Dependency,
        SimpleRoutingParent.Dependency,
        BackStackExample.Dependency,
        ParameterisedRoutingExample.Dependency,
        TransitionAnimationsExample.Dependency {
    }
}



