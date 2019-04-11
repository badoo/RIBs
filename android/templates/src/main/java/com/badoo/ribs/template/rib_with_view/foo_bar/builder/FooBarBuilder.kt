package com.badoo.ribs.template.rib_with_view.foo_bar.builder

import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBar
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarView

class FooBarBuilder(dependency: FooBar.Dependency) :
    Builder<FooBar.Dependency>(dependency) {

    fun build(): Node<FooBarView> {
        val customisation = dependency.ribCustomisation().get(FooBar.Customisation::class) ?: FooBar.Customisation()
        val component = DaggerFooBarComponent.factory()
            .create(dependency, customisation)

        return component.node()
    }
}
