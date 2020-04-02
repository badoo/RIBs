package com.badoo.ribs.example.rib.dialog_example.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.dialog_example.DialogExample
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView

class DialogExampleBuilder(
    dependency: DialogExample.Dependency
) : SimpleBuilder<DialogExample.Dependency, Node<DialogExampleView>>(object : DialogExample {}) {

    override val dependency : DialogExample.Dependency = object : DialogExample.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(DialogExample::class)
    }

    override fun build(buildParams: BuildParams<Nothing?>): Node<DialogExampleView> =
        DaggerDialogExampleComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(DialogExample.Customisation()),
                buildParams = buildParams
            )
            .node()
}
