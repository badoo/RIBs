package com.badoo.ribs.example.rib.dialog_example.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.dialog_example.DialogExample
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView

class DialogExampleBuilder(
    dependency: DialogExample.Dependency
) : Builder<DialogExample.Dependency, Nothing?, Node<DialogExampleView>>() {

    override val dependency : DialogExample.Dependency = object : DialogExample.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(DialogExample::class)
    }

    override fun build(params: BuildContext.ParamsWithData<Nothing?>): Node<DialogExampleView> =
        DaggerDialogExampleComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(DialogExample.Customisation()),
                buildContext = resolve(object : DialogExample {}, params)
            )
            .node()
}
