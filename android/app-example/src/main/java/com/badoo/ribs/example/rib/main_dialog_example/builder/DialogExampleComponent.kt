package com.badoo.ribs.example.rib.main_dialog_example.builder

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsum
import com.badoo.ribs.example.rib.main_dialog_example.DialogExample
import com.badoo.ribs.example.rib.main_dialog_example.DialogExampleView
import dagger.BindsInstance

@DialogExampleScope
@dagger.Component(
    modules = [DialogExampleModule::class],
    dependencies = [DialogExample.Dependency::class]
)
internal interface DialogExampleComponent : DialogLoremIpsum.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: DialogExample.Dependency,
            @BindsInstance customisation: DialogExample.Customisation,
            @BindsInstance savedInstanceState: Bundle?
        ): DialogExampleComponent
    }

    fun node(): Node<DialogExampleView>
}
