package com.badoo.ribs.example.rib.dialog_lorem_ipsum

import android.os.Bundle
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault

class DialogLoremIpsumBuilder(
    dependency: DialogLoremIpsum.Dependency
) : Builder<DialogLoremIpsum.Dependency>() {

    override val dependency : DialogLoremIpsum.Dependency = object : DialogLoremIpsum.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(DialogLoremIpsum::class)
    }

    fun build(savedInstanceState: Bundle?): Node<DialogLoremIpsumView> {
        val customisation = dependency.getOrDefault(DialogLoremIpsum.Customisation())
        val router = DialogLoremIpsumRouter(savedInstanceState)
        val interactor = DialogLoremIpsumInteractor(
            savedInstanceState,
            router,
            dependency.loremIpsumOutput()
        )

        return Node(
            savedInstanceState = savedInstanceState,
            identifier = object : DialogLoremIpsum {},
            viewFactory = customisation.viewFactory(null),
            router = router,
            interactor = interactor
        )
    }
}
