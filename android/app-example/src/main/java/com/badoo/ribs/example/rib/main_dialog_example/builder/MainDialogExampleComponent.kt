package com.badoo.ribs.example.rib.main_dialog_example.builder

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsum
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExample
import com.badoo.ribs.example.rib.main_dialog_example.MainDialogExampleView
import dagger.BindsInstance

@MainDialogExampleScope
@dagger.Component(
    modules = [MainDialogExampleModule::class],
    dependencies = [MainDialogExample.Dependency::class]
)
internal interface MainDialogExampleComponent : DialogLoremIpsum.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: MainDialogExample.Dependency,
            @BindsInstance customisation: MainDialogExample.Customisation,
            @BindsInstance savedInstanceState: Bundle?
        ): MainDialogExampleComponent
    }

    fun node(): Node<MainDialogExampleView>
}
