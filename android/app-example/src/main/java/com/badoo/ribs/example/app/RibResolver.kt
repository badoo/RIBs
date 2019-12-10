package com.badoo.ribs.example.app

import com.badoo.ribs.android.recyclerview.client.RecyclerViewRibResolver
import com.badoo.ribs.core.routing.action.AddToRecyclerViewRoutingAction.Companion.recyclerView
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.example.rib.foo_bar.builder.FooBarBuilder
import com.badoo.ribs.example.rib.lorem_ipsum.builder.LoremIpsumBuilder

class RibResolver(
    private val fooBarBuilder: FooBarBuilder,
    private val loremIpsumBuilder: LoremIpsumBuilder
) : RecyclerViewRibResolver<Item> {

    override fun resolve(element: Item): RoutingAction<RibView> =
        when (element) {
            Item.LoremIpsumItem -> recyclerView { loremIpsumBuilder.build(it) }
            Item.FooBarItem -> recyclerView { fooBarBuilder.build(it) }
        }
}
