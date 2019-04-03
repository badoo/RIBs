package com.badoo.ribs.example.rib.blocker.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.blocker.Blocker
import com.badoo.ribs.example.rib.blocker.BlockerView

class BlockerBuilder(dependency: Blocker.Dependency) :
    Builder<Blocker.Dependency>(dependency) {

    fun build(): Node<BlockerView> {
        val customisation = dependency.ribCustomisation().get(Blocker.Customisation::class) ?: Blocker.Customisation()
        val component = DaggerBlockerComponent.builder()
            .dependency(dependency)
            .customisation(customisation)
            .build()

        return component.node()
    }
}
