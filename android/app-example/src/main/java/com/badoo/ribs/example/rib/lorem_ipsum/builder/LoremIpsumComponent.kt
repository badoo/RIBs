package com.badoo.ribs.example.rib.lorem_ipsum.builder

import android.os.Bundle
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
            @BindsInstance savedInstanceState: Bundle?
        ): LoremIpsumComponent
    }

    fun node(): Node<LoremIpsumView>
}
