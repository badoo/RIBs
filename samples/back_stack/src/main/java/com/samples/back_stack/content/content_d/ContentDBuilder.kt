package com.samples.back_stack.content.content_d

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams

class ContentDBuilder(
    private val dependency: ContentD.Dependency
) : SimpleBuilder<ContentD>() {

    override fun build(buildParams: BuildParams<Nothing?>): ContentD {
        return ContentDNode(
            buildParams = buildParams,
            viewFactory = ContentDViewImpl.Factory().invoke(null),
            plugins = emptyList()
        )
    }

}
