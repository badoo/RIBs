package com.badoo.ribs.example.rib.lorem_ipsum.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView

@LoremIpsumScope
@dagger.Component(
    modules = [LoremIpsumModule::class],
    dependencies = [
        LoremIpsum.Dependency::class,
        LoremIpsum.Customisation::class
    ]
)
internal interface LoremIpsumComponent {

    @dagger.Component.Builder
    interface Builder {

        fun dependency(component: LoremIpsum.Dependency): Builder

        fun customisation(component: LoremIpsum.Customisation): Builder

        fun build(): LoremIpsumComponent
    }

    fun node(): Node<LoremIpsumView>
}
