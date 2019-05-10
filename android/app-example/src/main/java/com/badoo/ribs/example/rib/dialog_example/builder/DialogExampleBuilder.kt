package com.badoo.ribs.example.rib.dialog_example.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.customisationsBranchFor
import com.badoo.ribs.core.customisation.getOrDefault
import com.badoo.ribs.example.rib.dialog_example.DialogExample
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView

class DialogExampleBuilder(
    dependency: DialogExample.Dependency
) : Builder<DialogExample.Dependency>() {

    override val dependency : DialogExample.Dependency = object : DialogExample.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(DialogExample::class)
    }

    fun build(): Node<DialogExampleView> =
        DaggerDialogExampleComponent
            .factory()
            .create(dependency, dependency.getOrDefault(DialogExample.Customisation()))
            .node()
}
