package com.badoo.ribs.portal

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.test.emptyBuildParams
import org.mockito.kotlin.mock

open class TestNode(
    buildParams: BuildParams<*> = emptyBuildParams(),
    router: Router<*> = mock(),
    plugins: List<Plugin> = emptyList()
) : Node<Nothing>(
    buildParams = buildParams,
    viewFactory = null,
    plugins = plugins + listOf(router)
)
