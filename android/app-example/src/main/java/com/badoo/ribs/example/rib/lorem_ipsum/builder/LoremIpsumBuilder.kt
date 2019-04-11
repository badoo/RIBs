package com.badoo.ribs.example.rib.lorem_ipsum.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView

class LoremIpsumBuilder(dependency: LoremIpsum.Dependency) :
    Builder<LoremIpsum.Dependency>(dependency) {

    fun build(): Node<LoremIpsumView> {
        val customisation = dependency.ribCustomisation().get(LoremIpsum.Customisation::class) ?: LoremIpsum.Customisation()
        val component = DaggerLoremIpsumComponent.factory()
            .create(dependency, customisation)

        return component.node()
    }
}
