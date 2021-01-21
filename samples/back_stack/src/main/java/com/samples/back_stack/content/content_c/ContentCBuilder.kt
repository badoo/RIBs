package com.samples.back_stack.content.content_c

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class ContentCBuilder(
    private val dependency: ContentC.Dependency
) : SimpleBuilder<ContentC>() {

    override fun build(buildParams: BuildParams<Nothing?>): ContentC {
        return ContentCNode(
            buildParams = buildParams,
            viewFactory = ContentCViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )
    }

}
