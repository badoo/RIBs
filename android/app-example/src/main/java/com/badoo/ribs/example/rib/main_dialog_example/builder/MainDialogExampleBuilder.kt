package com.badoo.ribs.example.rib.main_dialog_example.builder

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExample
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExampleView

class MainDialogExampleBuilder(
    dependency: MainDialogExample.Dependency
) : Builder<MainDialogExample.Dependency>() {

    override val dependency : MainDialogExample.Dependency = object : MainDialogExample.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(MainDialogExample::class)
    }

    fun build(savedInstanceState: Bundle?): Node<MainDialogExampleView> =
        DaggerMainDialogExampleComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(MainDialogExample.Customisation()),
                savedInstanceState = savedInstanceState
            )
            .node()
}
