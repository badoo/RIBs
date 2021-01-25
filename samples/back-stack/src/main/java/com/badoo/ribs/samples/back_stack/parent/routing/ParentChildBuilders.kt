package com.badoo.ribs.samples.back_stack.parent.routing

import com.badoo.ribs.samples.back_stack.childrens.child_a.ChildA
import com.badoo.ribs.samples.back_stack.childrens.child_a.ChildABuilder
import com.badoo.ribs.samples.back_stack.childrens.child_b.ChildB
import com.badoo.ribs.samples.back_stack.childrens.child_b.ChildBBuilder
import com.badoo.ribs.samples.back_stack.childrens.child_c.ChildC
import com.badoo.ribs.samples.back_stack.childrens.child_c.ChildCBuilder
import com.badoo.ribs.samples.back_stack.childrens.child_d.ChildD
import com.badoo.ribs.samples.back_stack.childrens.child_d.ChildDBuilder
import com.badoo.ribs.samples.back_stack.childrens.child_e.ChildE
import com.badoo.ribs.samples.back_stack.childrens.child_e.ChildEBuilder
import com.badoo.ribs.samples.back_stack.childrens.child_f.ChildF
import com.badoo.ribs.samples.back_stack.childrens.child_f.ChildFBuilder
import com.badoo.ribs.samples.back_stack.parent.Parent

internal open class ParentChildBuilders(
    dependency: Parent.Dependency
) {

    private val subtreeDeps = SubtreeDependency(dependency)

    val contentA = ChildABuilder(subtreeDeps)
    val contentB = ChildBBuilder(subtreeDeps)
    val contentC = ChildCBuilder(subtreeDeps)
    val contentD = ChildDBuilder(subtreeDeps)

    val overlayE = ChildEBuilder(subtreeDeps)
    val overlayF = ChildFBuilder(subtreeDeps)

    class SubtreeDependency(
        dependency: Parent.Dependency
    ) : Parent.Dependency by dependency,
        ChildA.Dependency,
        ChildD.Dependency,
        ChildB.Dependency,
        ChildC.Dependency,
        ChildE.Dependency,
        ChildF.Dependency

}

