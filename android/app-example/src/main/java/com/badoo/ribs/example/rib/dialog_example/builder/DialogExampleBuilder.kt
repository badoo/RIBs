package com.badoo.ribs.example.rib.dialog_example.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.dialog_example.DialogExample
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView

class DialogExampleBuilder(dependency: DialogExample.Dependency) :
    Builder<DialogExample.Dependency>(dependency) {

    fun build(): Node<DialogExampleView> {
        val customisation = dependency.ribCustomisation().get(DialogExample.Customisation::class) ?: DialogExample.Customisation()
        val component = DaggerDialogExampleComponent.builder()
            .dependency(dependency)
            .customisation(customisation)
            .build()

        return component.node()
    }
}
