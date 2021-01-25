package com.badoo.ribs.samples.back_stack.content.content_b

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class ContentBBuilder(
    private val dependency: ContentB.Dependency
) : SimpleBuilder<ContentB>() {

    override fun build(buildParams: BuildParams<Nothing?>): ContentB {
        return ContentBNode(
            buildParams = buildParams,
            viewFactory = ContentBViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )
    }

}
