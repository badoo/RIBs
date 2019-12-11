package com.badoo.ribs.example.rib.main_foo_bar

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.main_foo_bar.viewplugin.ParentLongClickListener

class MainFooBarBuilder(
    dependency: MainFooBar.Dependency
) : Builder<MainFooBar.Dependency>() {

    override val dependency : MainFooBar.Dependency = object : MainFooBar.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(MainFooBar::class)
    }

    fun build(savedInstanceState: Bundle?): Node<MainFooBarView> {
        val customisation = dependency.getOrDefault(MainFooBar.Customisation())
        val router = MainFooBarRouter(savedInstanceState)
        val interactor = MainFooBarInteractor(
            savedInstanceState,
            router,
            dependency.permissionRequester()
        )
        val viewPlugins = setOf(ParentLongClickListener())

        return MainFooBarNode(
            customisation.viewFactory(null),
            router,
            interactor,
            savedInstanceState,
            viewPlugins
        )
    }
}
