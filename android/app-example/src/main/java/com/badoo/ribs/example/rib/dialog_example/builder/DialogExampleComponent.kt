package com.badoo.ribs.example.rib.dialog_example.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.dialog_example.DialogExample
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum

@DialogExampleScope
@dagger.Component(
    modules = [DialogExampleModule::class],
    dependencies = [
        DialogExample.Dependency::class,
        DialogExample.Customisation::class
    ]
)
internal interface DialogExampleComponent : LoremIpsum.Dependency {

    @dagger.Component.Builder
    interface Builder {

        fun dependency(component: DialogExample.Dependency): Builder

        fun customisation(component: DialogExample.Customisation): Builder

        fun build(): DialogExampleComponent
    }

    fun node(): Node<DialogExampleView>
}
