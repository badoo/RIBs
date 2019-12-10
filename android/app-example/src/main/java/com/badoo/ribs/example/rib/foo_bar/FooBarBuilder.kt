package com.badoo.ribs.example.rib.foo_bar

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.foo_bar.viewplugin.ParentLongClickListener

class FooBarBuilder(
    dependency: FooBar.Dependency
) : Builder<FooBar.Dependency>() {

    override val dependency : FooBar.Dependency = object : FooBar.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(FooBar::class)
    }

    fun build(savedInstanceState: Bundle?): Node<FooBarView> {
        val customisation = dependency.getOrDefault(FooBar.Customisation())
        val router = FooBarRouter(savedInstanceState)
        val interactor = FooBarInteractor(
            savedInstanceState,
            router,
            dependency.permissionRequester()
        )
        val viewPlugins = setOf(ParentLongClickListener())

        return FooBarNode(
            customisation.viewFactory(null),
            router,
            interactor,
            savedInstanceState,
            viewPlugins
        )
    }
}
