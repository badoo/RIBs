package com.badoo.ribs.portal

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.routing.router.Router
import com.nhaarman.mockitokotlin2.mock

open class TestNode(
    router: Router<*> = mock(),
    plugins: List<Plugin> = emptyList()
) : Node<Nothing>(
    buildParams = BuildParams.Empty(),
    viewFactory = null,
    plugins = plugins + listOf(router)
)
