package com.badoo.ribs.samples.transitionanimations.rib.parent.routing

import com.badoo.ribs.samples.transitionanimations.rib.child1.Child1Builder
import com.badoo.ribs.samples.transitionanimations.rib.child2.Child2Builder
import com.badoo.ribs.samples.transitionanimations.rib.child3.Child3Builder

internal open class ParentChildBuilders {

    val child1Builder: Child1Builder = Child1Builder()
    val child2Builder: Child2Builder = Child2Builder()
    val child3Builder: Child3Builder = Child3Builder()

}