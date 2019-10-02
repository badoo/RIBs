package com.badoo.ribs.example.rib.dialog_example.builder

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.dialog_example.DialogExample
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import dagger.BindsInstance

@DialogExampleScope
@dagger.Component(
    modules = [DialogExampleModule::class],
    dependencies = [DialogExample.Dependency::class]
)
internal interface DialogExampleComponent : LoremIpsum.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: DialogExample.Dependency,
            @BindsInstance customisation: DialogExample.Customisation,
            @BindsInstance buildContext: BuildContext.Resolved<Nothing?>
        ): DialogExampleComponent
    }

    fun node(): Node<DialogExampleView>
}
