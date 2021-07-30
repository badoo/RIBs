package com.badoo.ribs.samples.back_stack.rib.back_stack_example.routing

import com.badoo.ribs.samples.back_stack.rib.child_a.ChildA
import com.badoo.ribs.samples.back_stack.rib.child_a.ChildABuilder
import com.badoo.ribs.samples.back_stack.rib.child_b.ChildB
import com.badoo.ribs.samples.back_stack.rib.child_b.ChildBBuilder
import com.badoo.ribs.samples.back_stack.rib.child_c.ChildC
import com.badoo.ribs.samples.back_stack.rib.child_c.ChildCBuilder
import com.badoo.ribs.samples.back_stack.rib.child_d.ChildD
import com.badoo.ribs.samples.back_stack.rib.child_d.ChildDBuilder
import com.badoo.ribs.samples.back_stack.rib.child_e.ChildE
import com.badoo.ribs.samples.back_stack.rib.child_e.ChildEBuilder
import com.badoo.ribs.samples.back_stack.rib.child_f.ChildF
import com.badoo.ribs.samples.back_stack.rib.child_f.ChildFBuilder
import com.badoo.ribs.samples.back_stack.rib.back_stack_example.BackStackExample

internal open class BackStackExampleChildBuilders(
    dependency: BackStackExample.Dependency
) {

    private val subtreeDeps = SubtreeDependency(dependency)

    val childA: ChildABuilder = ChildABuilder(subtreeDeps)
    val childB: ChildBBuilder = ChildBBuilder(subtreeDeps)
    val childC: ChildCBuilder = ChildCBuilder(subtreeDeps)
    val childD: ChildDBuilder = ChildDBuilder(subtreeDeps)
    val childE: ChildEBuilder = ChildEBuilder(subtreeDeps)
    val childF: ChildFBuilder = ChildFBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: BackStackExample.Dependency
    ) : BackStackExample.Dependency by dependency,
        ChildA.Dependency,
        ChildD.Dependency,
        ChildB.Dependency,
        ChildC.Dependency,
        ChildE.Dependency,
        ChildF.Dependency

}

