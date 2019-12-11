package com.badoo.ribs.example.rib.main_dialog_example.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.main_dialog_example.DialogExample
import com.badoo.ribs.example.rib.main_dialog_example.DialogExampleView

class DialogExampleBuilder(
    dependency: DialogExample.Dependency
) : Builder<DialogExample.Dependency>() {

    override val dependency : DialogExample.Dependency = object : DialogExample.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(DialogExample::class)
    }

    fun build(savedInstanceState: Bundle?): Node<DialogExampleView> =
        DaggerDialogExampleComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(DialogExample.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
