package com.badoo.ribs.example.rib.lorem_ipsum.builder

import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView
import dagger.BindsInstance

@LoremIpsumScope
@dagger.Component(
    modules = [LoremIpsumModule::class],
    dependencies = [LoremIpsum.Dependency::class]
)
internal interface LoremIpsumComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: LoremIpsum.Dependency,
            @BindsInstance customisation: LoremIpsum.Customisation,
            @BindsInstance buildContext: BuildContext.Resolved<Nothing?>
        ): LoremIpsumComponent
    }

    fun node(): Node<LoremIpsumView>
}
