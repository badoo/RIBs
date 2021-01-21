package com.samples.back_stack.content.content_a

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class ContentABuilder(
    private val dependency: ContentA.Dependency
) : SimpleBuilder<ContentA>() {

    override fun build(buildParams: BuildParams<Nothing?>): ContentA {
        return ContentANode(
            buildParams = buildParams,
            viewFactory = ContentAViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )
    }

}
